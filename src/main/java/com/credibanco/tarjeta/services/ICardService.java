package com.credibanco.tarjeta.services;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;

import com.credibanco.tarjeta.model.Card;
import com.credibanco.tarjeta.response.CardResponseRest;


public interface ICardService {

	public ResponseEntity<CardResponseRest> search();
	public ResponseEntity<CardResponseRest> searchById(Long id);
	public ResponseEntity<CardResponseRest> save(Card card);
	public ResponseEntity<CardResponseRest> update(Card card, Long id);
	public ResponseEntity<CardResponseRest> deleteById(Long id);
	public ResponseEntity<CardResponseRest> searchBynumber(String cardId);
	
	public String generateCardNumber(Long productId);
	public ResponseEntity<CardResponseRest> activateCard(String cardId);
    public ResponseEntity<CardResponseRest> blockCard(String cardId);
    public ResponseEntity<CardResponseRest> rechargeBalance(String cardId, BigDecimal balance);
    public ResponseEntity<?> searchBalance(String cardId);
  
	
}
