package com.credibanco.tarjeta.services;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;

import com.credibanco.tarjeta.model.Transaction;
import com.credibanco.tarjeta.response.TransactionResponseRest;


public interface ITransactionService {
	
	public ResponseEntity<TransactionResponseRest> search();
	public ResponseEntity<TransactionResponseRest> searchById(Long id);
	public ResponseEntity<TransactionResponseRest> save(Transaction transaction, Long id);
	public ResponseEntity<TransactionResponseRest> update(Transaction transaction, Long Id, Long idCard);
	public ResponseEntity<TransactionResponseRest> deleteById(Long id);
	
	public ResponseEntity<TransactionResponseRest> savePurchase(String cardId, BigDecimal price);
	public ResponseEntity<String> anulateTransaction(String cardId, Long transactionId);
	

}
