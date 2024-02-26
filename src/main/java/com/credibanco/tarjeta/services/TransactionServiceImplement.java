package com.credibanco.tarjeta.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.credibanco.tarjeta.dao.ICardDao;
import com.credibanco.tarjeta.dao.ITransactionDao;
import com.credibanco.tarjeta.model.Card;
import com.credibanco.tarjeta.model.Transaction;
import com.credibanco.tarjeta.response.TransactionResponseRest;
import com.credibanco.tarjeta.utils.ApiResponseCode;
import com.credibanco.tarjeta.utils.ResponseConverter;
import com.credibanco.tarjeta.utils.TimeUtil;

@Service
public class TransactionServiceImplement implements ITransactionService{
	
	 @Autowired
	 private TimeUtil timeUtil;

	private ICardDao cardDao;
	private ITransactionDao transactionDao;

	public TransactionServiceImplement(ICardDao cardDao, ITransactionDao transactionalDao) {
		super();
		this.cardDao = cardDao;
		this.transactionDao = transactionalDao;
	}
	
	
	@Override
	@Transactional
	public ResponseEntity<TransactionResponseRest> savePurchase(String cardId, BigDecimal price) {
		TransactionResponseRest response = new TransactionResponseRest();
		List<Transaction> list = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();
			
		Optional<Card> cardSearch = cardDao.findByCardNumber(cardId);
		
		if(cardSearch.isPresent() 
	    		&& now.isBefore(cardSearch.get().getExpirationDate())
	            && cardSearch.get().getLockedCard()
	            && cardSearch.get().getStatusCard()
	            && cardSearch.get().getBalance().compareTo(BigDecimal.ZERO)>0) {
			
			Transaction transaction = new Transaction();
			
			transaction.setCard(cardSearch.get());
			transaction.setTransactionDate(now);
			transaction.setTransactionType(1);
			transaction.setValue(price);
			transaction.setStatusTransaction(1);
			
			Transaction transactionSaved = transactionDao.save(transaction);
			
			if (transactionSaved != null) {
				list.add(transactionSaved);
				response.getTransactionResponse().setTransactional(list);
				response.setHead(ApiResponseCode.SUCCESS.getStatus(), ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage());
			} else {
				response.setHead(ApiResponseCode.BAD_REQUEST.getStatus(), ApiResponseCode.BAD_REQUEST.getCode(), ApiResponseCode.BAD_REQUEST.getMessage());
				return new ResponseEntity<TransactionResponseRest>(response, HttpStatus.BAD_REQUEST);
			}
			
		} else {
			response.setHead(ApiResponseCode.NOT_FOUND.getStatus(), ApiResponseCode.NOT_FOUND.getCode(), ApiResponseCode.NOT_FOUND.getMessage());
			return new ResponseEntity<TransactionResponseRest>(response, HttpStatus.NOT_FOUND);
		}
						
		
		return new ResponseEntity<TransactionResponseRest>(response, HttpStatus.OK);
	}
	
	@Override
	@Transactional (readOnly = true)
	public ResponseEntity<TransactionResponseRest> searchById(Long id) {

		TransactionResponseRest response = new TransactionResponseRest();
		List<Transaction> list = new ArrayList<>();
		
			
			Optional<Transaction> transactional = transactionDao.findById(id);
			
			if (transactional.isPresent()) {
				list.add(transactional.get());
				response.getTransactionResponse().setTransactional(list);
				response.setHead(ApiResponseCode.SUCCESS.getStatus(), ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage());
			} else {
				response.setHead(ApiResponseCode.NOT_FOUND.getStatus(), ApiResponseCode.NOT_FOUND.getCode(), ApiResponseCode.NOT_FOUND.getMessage());
				return new ResponseEntity<TransactionResponseRest>(response, HttpStatus.NOT_FOUND);
			}
				
		return new ResponseEntity<TransactionResponseRest>(response, HttpStatus.OK);
		
	}
	
	@Override
	@Transactional
	public ResponseEntity<String> anulateTransaction(String cardId, Long transactionId) {
		
		TransactionResponseRest response = new TransactionResponseRest();
		String responseBody = ResponseConverter.convertTransactionResponseToString(response);
		List<Transaction> list = new ArrayList<>();
		
		Optional<Transaction> transactionSearch = transactionDao.findById(transactionId);
		
		if (transactionSearch.isPresent() 
			&& timeUtil.isWithin24Hours(transactionSearch.get().getTransactionDate())) {
			
			Optional<Card> cardSearch = cardDao.findByCardNumber(cardId);
			
			Transaction transactionToUpdate = transactionSearch.get();
	        transactionToUpdate.setStatusTransaction(0);
	        
	        transactionDao.save(transactionToUpdate);
	        
			if(cardSearch.isPresent()){
				
				Card cardToUpdate = cardSearch.get();
				BigDecimal total=cardSearch.get().getBalance().add(transactionToUpdate.getValue());
				
				cardToUpdate.setBalance(total);
				cardDao.save(cardToUpdate);
				
				response.getTransactionResponse().setTransactional(list);
				response.setHead(ApiResponseCode.SUCCESS.getStatus(), ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage());
			}else{
				response.setHead(ApiResponseCode.NOT_FOUND.getStatus(), ApiResponseCode.NOT_FOUND.getCode(), ApiResponseCode.NOT_FOUND.getMessage());
				return new ResponseEntity<String>(responseBody, HttpStatus.NOT_FOUND);
				
			}
			response.getTransactionResponse().setTransactional(list);
			response.setHead(ApiResponseCode.SUCCESS.getStatus(), ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage());
		} else {
			response.setHead(ApiResponseCode.NOT_FOUND.getStatus(), ApiResponseCode.NOT_FOUND.getCode(), ApiResponseCode.NOT_FOUND.getMessage());
			return new ResponseEntity<String>(responseBody, HttpStatus.NOT_FOUND);
		}
		
		 return new ResponseEntity<String>(responseBody, HttpStatus.OK);
		
	} 
	
	@Override
	@Transactional (readOnly = true)
	public ResponseEntity<TransactionResponseRest> search() {
		
		TransactionResponseRest response = new TransactionResponseRest();
		
		List<Transaction> transaction = (List<Transaction>) transactionDao.findAll();
		
		response.getTransactionResponse().setTransactional(transaction);
		response.setHead(ApiResponseCode.SUCCESS.getStatus(), ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage());
			
		return new ResponseEntity<TransactionResponseRest>(response, HttpStatus.OK);
	}

	

	@Override
	@Transactional
	public ResponseEntity<TransactionResponseRest> save(Transaction transaction, Long idCard) {
		
		TransactionResponseRest response = new TransactionResponseRest();
		List<Transaction> list = new ArrayList<>();
			
		Optional<Card> card = cardDao.findById(idCard);
		
		if( card.isPresent()) {
			transaction.setCard(card.get());
			
			Transaction transactionSaved = transactionDao.save(transaction);
			
			if (transactionSaved != null) {
				list.add(transactionSaved);
				response.getTransactionResponse().setTransactional(list);
				response.setHead(ApiResponseCode.SUCCESS.getStatus(), ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage());
			} else {
				response.setHead(ApiResponseCode.BAD_REQUEST.getStatus(), ApiResponseCode.BAD_REQUEST.getCode(), ApiResponseCode.BAD_REQUEST.getMessage());
				return new ResponseEntity<TransactionResponseRest>(response, HttpStatus.BAD_REQUEST);
			}
			
		} else {
			response.setHead(ApiResponseCode.NOT_FOUND.getStatus(), ApiResponseCode.NOT_FOUND.getCode(), ApiResponseCode.NOT_FOUND.getMessage());
			return new ResponseEntity<TransactionResponseRest>(response, HttpStatus.NOT_FOUND);
		}
						
		
		return new ResponseEntity<TransactionResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<TransactionResponseRest> update(Transaction transaction, Long id, Long idCard) {
	    TransactionResponseRest response = new TransactionResponseRest();

        Optional<Transaction> transactionSearch = transactionDao.findById(id);
        Optional<Card> cardSearch = cardDao.findById(idCard);

	        if (!transactionSearch.isPresent()) {
	            response.setHead(ApiResponseCode.NOT_FOUND.getStatus(), ApiResponseCode.NOT_FOUND.getCode(), ApiResponseCode.NOT_FOUND.getMessage());
	            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	        }

	        if (!transactionSearch.get().getCard().getId_card().equals(cardSearch.get().getId_card())) {
	            response.setHead(ApiResponseCode.NOT_FOUND.getStatus(), ApiResponseCode.NOT_FOUND.getCode(), ApiResponseCode.NOT_FOUND.getMessage());
	            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	        }

        Transaction transactionToUpdate = transactionSearch.get();
        transactionToUpdate.setTransactionDate(transaction.getTransactionDate());
        transactionToUpdate.setTransactionType(transaction.getTransactionType());
        transactionToUpdate.setValue(transaction.getValue());
        transactionToUpdate.setStatusTransaction(transaction.getStatusTransaction());

        transactionDao.save(transactionToUpdate);
        response.setHead(ApiResponseCode.SUCCESS.getStatus(), ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage());
        response.getTransactionResponse().setTransactional(Collections.singletonList(transactionToUpdate));
        
        return new ResponseEntity<>(response, HttpStatus.OK);

	}
		


	@Override
	@Transactional
	public ResponseEntity<TransactionResponseRest> deleteById(Long id) {
		
		TransactionResponseRest response = new TransactionResponseRest();
		

	    if (!transactionDao.existsById(id)) {
	        response.setHead(ApiResponseCode.BAD_REQUEST.getStatus(), ApiResponseCode.BAD_REQUEST.getCode(), ApiResponseCode.BAD_REQUEST.getMessage());
	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    }else {
			transactionDao.deleteById(id);
			response.setHead(ApiResponseCode.SUCCESS.getStatus(), ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage());
	    }			

		return new ResponseEntity<TransactionResponseRest>(response, HttpStatus.OK);
	}

}
