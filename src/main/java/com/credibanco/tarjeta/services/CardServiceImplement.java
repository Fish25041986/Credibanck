package com.credibanco.tarjeta.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.credibanco.tarjeta.dao.ICardDao;
import com.credibanco.tarjeta.dto.CardDto;
import com.credibanco.tarjeta.model.Card;
import com.credibanco.tarjeta.response.CardResponseRest;
import com.credibanco.tarjeta.response.GenericObjectResponseRest;
import com.credibanco.tarjeta.utils.ApiResponseCode;
import com.credibanco.tarjeta.utils.CardNumberGenerator;





@Service
public class CardServiceImplement implements ICardService{


	@Autowired
	private ICardDao cardDao;
	
	@Override
	@Transactional
	public ResponseEntity<CardResponseRest> save(Card card) {
		
		CardResponseRest response = new CardResponseRest();
		List<Card> list = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime afterThreeYears = now.plusYears(3);
		BigDecimal initialBalance = new BigDecimal("0.00");
		
		card.setCreationDate(now);
		card.setExpirationDate(afterThreeYears);
		card.setStatusCard(false);
		card.setLockedCard(true);
		card.setBalance(initialBalance);

		
		Card cardSaved = cardDao.save(card);
		
		if (cardSaved != null) {
			list.add(cardSaved);
			response.getCardResponse().setCard(list);
			response.setHead(ApiResponseCode.SUCCESS.getStatus(), ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage());
		} else {
			response.setHead(ApiResponseCode.NOT_FOUND.getStatus(),ApiResponseCode.NOT_FOUND.getCode(),ApiResponseCode.NOT_FOUND.getMessage() );
			return new ResponseEntity<CardResponseRest>(response, HttpStatus.NOT_FOUND);
		}

		
		return new ResponseEntity<CardResponseRest>(response, HttpStatus.OK);
	}
	
	@Override
	@Transactional
	public String generateCardNumber(Long productId) {
		return CardNumberGenerator.generateCardNumber(productId);
	}
	
	
	@Override
	@Transactional
	public ResponseEntity<CardResponseRest> activateCard(String cardId) {
		
		CardResponseRest response = new CardResponseRest();
		
		
		Optional<Card> cardSearch = cardDao.findByCardNumber(cardId);
		
		if (cardSearch.isPresent()) {
			Card card = cardSearch.get();
			
			 if (!card.getStatusCard()) {
		            card.setStatusCard(true);
		            cardDao.save(card);
		            response.setHead(ApiResponseCode.SUCCESS.getStatus(), ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage());
		        } else {
		            response.setHead(ApiResponseCode.SUCCESS.getStatus(), ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage());
		        }
		} else {
			response.setHead(ApiResponseCode.NOT_FOUND.getStatus(), ApiResponseCode.NOT_FOUND.getCode(), ApiResponseCode.NOT_FOUND.getMessage());
			return new ResponseEntity<CardResponseRest>(response, HttpStatus.NOT_FOUND);
		}
		
        return new ResponseEntity<CardResponseRest>(response, HttpStatus.OK);
}
	

	@Override
	@Transactional
	public ResponseEntity<CardResponseRest> blockCard(String cardId) {
	    
	    CardResponseRest response = new CardResponseRest();
	    
	    Optional<Card> cardSearch = cardDao.findByCardNumber(cardId);
	    
	    if (cardSearch.isPresent()) {
	        Card card = cardSearch.get();
	        
	        if (card.getLockedCard()) {
	            card.setLockedCard(false);
	            cardDao.save(card);
	            response.setHead(ApiResponseCode.SUCCESS.getStatus(), ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage());
	        } else {
	            response.setHead(ApiResponseCode.SUCCESS.getStatus(), ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage());
	        }
	        return new ResponseEntity<CardResponseRest>(response, HttpStatus.OK);
	    } else {
	        response.setHead(ApiResponseCode.NOT_FOUND.getStatus(), ApiResponseCode.NOT_FOUND.getCode(), ApiResponseCode.NOT_FOUND.getMessage());
	        return new ResponseEntity<CardResponseRest>(response, HttpStatus.NOT_FOUND);
	    }
	    
	}

	
	@Override
	@Transactional
	public ResponseEntity<CardResponseRest> rechargeBalance(String cardId, BigDecimal balance) {
		
		CardResponseRest response = new CardResponseRest();	
        Optional<Card> cardSearch = cardDao.findByCardNumber(cardId);
        LocalDateTime now = LocalDateTime.now();
        
	    if (cardSearch.isPresent() 
	    		&& now.isBefore(cardSearch.get().getExpirationDate())
	            && cardSearch.get().getLockedCard()
	            && cardSearch.get().getStatusCard()){
	    	
			BigDecimal total=cardSearch.get().getBalance().add(balance);
			Card card = cardSearch.get();
			
			card.setBalance(total);
			cardDao.save(card);
			
            response.setHead(ApiResponseCode.SUCCESS.getStatus(), ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage());
	    } else {
	        response.setHead(ApiResponseCode.NOT_FOUND.getStatus(), ApiResponseCode.NOT_FOUND.getCode(), ApiResponseCode.NOT_FOUND.getMessage());
	        return new ResponseEntity<CardResponseRest>(response, HttpStatus.NOT_FOUND);
	    }
        return new ResponseEntity<CardResponseRest>(response, HttpStatus.OK);
	}
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<?> searchBalance(String cardId) {
		
		GenericObjectResponseRest<CardDto> response = new GenericObjectResponseRest<>();
		List<CardDto> list = new ArrayList<>();
		
	
		Optional<Card> cardSearch = cardDao.findByCardNumber(cardId);
		
		if (cardSearch.isPresent()) {
			Card card = cardSearch.get();

			CardDto cardRequest = new CardDto(card.getCardNumber(), card.getBalance());
	        list.add(cardRequest);

	        response.getGenericObjectResponse().setGenericObject(list);
			response.setHead(ApiResponseCode.SUCCESS.getStatus(), ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage());
		} else {
			response.setHead(ApiResponseCode.NOT_FOUND.getStatus(), ApiResponseCode.NOT_FOUND.getCode(), ApiResponseCode.NOT_FOUND.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
			
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<CardResponseRest> search() {
		
		CardResponseRest response = new CardResponseRest();
    
		List<Card> cards = (List<Card>) cardDao.findAll();
		response.getCardResponse().setCard(cards);
		response.setHead(ApiResponseCode.SUCCESS.getStatus(), ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage());
				
		return new ResponseEntity<CardResponseRest>(response, HttpStatus.OK);
		
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<CardResponseRest> searchById(Long id) {
		
		CardResponseRest response = new CardResponseRest();
		List<Card> list = new ArrayList<>();
		
	
		Optional<Card> card = cardDao.findById(id);
		
		if (card.isPresent()) {
			list.add(card.get());
			response.getCardResponse().setCard(list);
			response.setHead(ApiResponseCode.SUCCESS.getStatus(), ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage());
		} else {
			response.setHead(ApiResponseCode.NOT_FOUND.getStatus(), ApiResponseCode.NOT_FOUND.getCode(), ApiResponseCode.NOT_FOUND.getMessage());
			return new ResponseEntity<CardResponseRest>(response, HttpStatus.NOT_FOUND);
		}
			
		return new ResponseEntity<CardResponseRest>(response, HttpStatus.OK);
		
	}
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<CardResponseRest> searchBynumber(String cardId) {
		
		CardResponseRest response = new CardResponseRest();
		List<Card> list = new ArrayList<>();
		
	
		Optional<Card> card = cardDao.findByCardNumber(cardId);
		
		if (card.isPresent()) {
			list.add(card.get());
			response.getCardResponse().setCard(list);
			response.setHead(ApiResponseCode.SUCCESS.getStatus(), ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage());
		} else {
			response.setHead(ApiResponseCode.NOT_FOUND.getStatus(), ApiResponseCode.NOT_FOUND.getCode(), ApiResponseCode.NOT_FOUND.getMessage());
			return new ResponseEntity<CardResponseRest>(response, HttpStatus.NOT_FOUND);
		}
			
		return new ResponseEntity<CardResponseRest>(response, HttpStatus.OK);
	}
	

	@Override
	@Transactional
	public ResponseEntity<CardResponseRest> update(Card card, Long id) {

		CardResponseRest response = new CardResponseRest();
		List<Card> list = new ArrayList<>();
			
		Optional<Card> cardSearch = cardDao.findById(id);
		
		if (cardSearch.isPresent()) {
			
			cardSearch.get().setCardType(card.getCardType());
			cardSearch.get().setCardNumber(card.getCardNumber());
			cardSearch.get().setClientName(card.getClientName());
			cardSearch.get().setClientLastName(card.getClientLastName());
			cardSearch.get().setCreationDate(card.getCreationDate());
			cardSearch.get().setExpirationDate(card.getExpirationDate());
			cardSearch.get().setStatusCard(card.getStatusCard());
			cardSearch.get().setLockedCard(card.getLockedCard());
			cardSearch.get().setBalance(card.getBalance());
		
			Card cardToUpdate = cardDao.save(cardSearch.get());
			
			if (cardToUpdate != null) {
				list.add(cardToUpdate);
				response.getCardResponse().setCard(list);
				response.setHead(ApiResponseCode.SUCCESS.getStatus(), ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage());
			} else {
				response.setHead(ApiResponseCode.NOT_FOUND.getStatus(), ApiResponseCode.NOT_FOUND.getCode(), ApiResponseCode.NOT_FOUND.getMessage());
				return new ResponseEntity<CardResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
			
		} else {
			response.setHead(ApiResponseCode.NOT_FOUND.getStatus(), ApiResponseCode.NOT_FOUND.getCode(), ApiResponseCode.NOT_FOUND.getMessage());
			return new ResponseEntity<CardResponseRest>(response, HttpStatus.NOT_FOUND);
		}

		
		return new ResponseEntity<CardResponseRest>(response, HttpStatus.OK);
		
	}

	@Override
	@Transactional
	public ResponseEntity<CardResponseRest> deleteById(Long id) {
		
		CardResponseRest response = new CardResponseRest();
		
		if (!cardDao.existsById(id)) {
	        response.setHead(ApiResponseCode.BAD_REQUEST.getStatus(), ApiResponseCode.BAD_REQUEST.getCode(), ApiResponseCode.BAD_REQUEST.getMessage());
	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    }else {
	    	cardDao.deleteById(id);
			response.setHead(ApiResponseCode.SUCCESS.getStatus(), ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage());
	    }	
			
		
		return new ResponseEntity<CardResponseRest>(response, HttpStatus.OK);
	}

	
}
