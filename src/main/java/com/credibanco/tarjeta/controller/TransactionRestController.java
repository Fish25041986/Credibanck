package com.credibanco.tarjeta.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.credibanco.tarjeta.model.Transaction;
import com.credibanco.tarjeta.response.TransactionResponseRest;
import com.credibanco.tarjeta.services.ITransactionService;


@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1")
public class TransactionRestController {

	private ITransactionService transactionService;	
	
	public TransactionRestController(ITransactionService transactionService) {
		super();
		this.transactionService = transactionService;
	}

	/**
	 * 
	 * @param purchase
	 * @param idCard
	 * @param value
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/transactions/purchase")
	public ResponseEntity<TransactionResponseRest> purchase(
			@RequestParam("cardId") String cardId,
            @RequestParam("price") BigDecimal price) throws IOException{
		ResponseEntity<TransactionResponseRest> response = transactionService.savePurchase(cardId, price);
		return response;
	}
	
	/**
	 * 
	 * @param anulation
	 * @param cardId
	 * @param transactionId
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/transactions/anulation")
    public ResponseEntity<String> anulateTransaction(
            @RequestParam("cardId") String cardId,
            @RequestParam("transactionId") Long transactionId) {
        return transactionService.anulateTransaction(cardId, transactionId);
    }
	
	/**
	 * search by id
	 * @param id
	 * @return
	 */
	@GetMapping("/transactions/{id}")
	public ResponseEntity<TransactionResponseRest> searchById(@PathVariable Long id){
		ResponseEntity<TransactionResponseRest> response = transactionService.searchById(id);
		return response;
	}
	
	/**
	 * get transaction
	 * @return
	 */
	@GetMapping("/transactions")
	public ResponseEntity<TransactionResponseRest> search(){
		ResponseEntity<TransactionResponseRest> response = transactionService.search();
		return response;
	}
	
	
	/**
	 * 
	 * @param transaction
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/transactions")
	public ResponseEntity<TransactionResponseRest> save(
			@RequestParam("idCard") Long idCard,
			@RequestParam("transactionDate")
		    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime transactionDate,
			@RequestParam("transactionType") Integer transactionType,
			@RequestParam("value") BigDecimal value,
			@RequestParam("statusTransaction") Integer statusTransaction
			) throws IOException{
		Transaction transactional= new Transaction();
		transactional.setTransactionDate(transactionDate);
		transactional.setTransactionType(transactionType);
		transactional.setValue(value);
		transactional.setStatusTransaction(statusTransaction);
		
		
		ResponseEntity<TransactionResponseRest> response = transactionService.save(transactional, idCard);
		return response;
	}
	
	/**
	 * 
	 * @param transaction
	 * @return
	 * @throws IOException
	 */
	@PutMapping("/transactions")
	public ResponseEntity<TransactionResponseRest> update(
			@RequestParam("id") Long id,
			@RequestParam("idCard") Long idCard,
			@RequestParam("transactionDate")
		    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime transactionDate,
			@RequestParam("transactionType") Integer transactionType,
			@RequestParam("value") BigDecimal value,
			@RequestParam("statusTransaction") Integer statusTransaction
			) throws IOException{
		Transaction transactional= new Transaction();
		transactional.setTransactionDate(transactionDate);
		transactional.setTransactionType(transactionType);
		transactional.setValue(value);
		transactional.setStatusTransaction(statusTransaction);
		
		
		ResponseEntity<TransactionResponseRest> response = transactionService.update(transactional,id, idCard);
		return response;
	}
	
	/**
	 * delete by id
	 * @param id
	 * @return
	 */
	@DeleteMapping("/transactions/{id}")
	public ResponseEntity<TransactionResponseRest> deleteById(@PathVariable Long id){
		ResponseEntity<TransactionResponseRest> response = transactionService.deleteById(id);
		return response;
	}
	
	
}
