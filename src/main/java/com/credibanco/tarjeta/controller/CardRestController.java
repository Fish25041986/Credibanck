package com.credibanco.tarjeta.controller;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.credibanco.tarjeta.model.Card;
import com.credibanco.tarjeta.response.CardResponseRest;
import com.credibanco.tarjeta.services.ICardService;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1")
public class CardRestController {

	    private ICardService cardService;	
		
		public CardRestController(ICardService cardService) {
			super();
			this.cardService = cardService;
		}
		
		/**
		 * 
		 * @param card
		 * @return
		 * @throws IOException
		 */
		@PostMapping("/cards")
		public ResponseEntity<CardResponseRest> save(@RequestBody Card card) throws IOException{
			
			ResponseEntity<CardResponseRest> response = cardService.save(card);
			return response;
		}
		
		/**
		 * generate card number
		 * @param id
		 * @return
		 */
		@GetMapping("/{productId}/number")
	    public ResponseEntity<String> generateCardNumber(@PathVariable Long productId) {
			String cardNumber = cardService.generateCardNumber(productId);
	        return ResponseEntity.ok(cardNumber);
	    }
		
		/**
		 * activate card 
		 * @param id
		 * @return
		 */
		@PostMapping("/cards/enroll")
		public ResponseEntity<CardResponseRest> activateCard(
				 @RequestParam("cardId") String cardId) {
		    ResponseEntity<CardResponseRest> response = cardService.activateCard(cardId);
		    return response;
		}
		
		/**
		 * block card 
		 * @param id
		 * @return
		 */
		@DeleteMapping("/cards/{cardId}")
		public ResponseEntity<CardResponseRest> blockCard(@PathVariable String cardId) {
			ResponseEntity<CardResponseRest> response = cardService.blockCard(cardId);
			return response;
		}
		
		/**
		 * recharges balance 
		 * @param id
		 * @return
		 */
		@PostMapping("/cards/balance")
		public ResponseEntity<CardResponseRest> rechargeBalance(
				 @RequestParam("cardId") String cardId,
		         @RequestParam("balance") BigDecimal balance) {
		    ResponseEntity<CardResponseRest> response = cardService.rechargeBalance(cardId, balance);
		    return response;
		}
		
		/**
		 * search by id
		 * @param id
		 * @return
		 */
		@GetMapping("/cards/balance/{id}")
		public ResponseEntity<?> searchBalance(@PathVariable String id){
			ResponseEntity<?> response = cardService.searchBalance(id);
			return response;
		}
		
		
		/**
		 * get cards
		 * @return
		 */
		@GetMapping("/cards")
		public ResponseEntity<CardResponseRest> search(){
			ResponseEntity<CardResponseRest> response = cardService.search();
			return response;
		}
		
		/**
		 * search by id
		 * @param id
		 * @return
		 */
		@GetMapping("/cards/{id}")
		public ResponseEntity<CardResponseRest> searchById(@PathVariable Long id){
			ResponseEntity<CardResponseRest> response = cardService.searchById(id);
			return response;
		}
		
		
		
		/**
		 * update card
		 * @param id
		 * @return
		 * @throws IOException
		 */
		@PutMapping("/cards/{id}")
		public ResponseEntity<CardResponseRest> update(@RequestBody Card card, @PathVariable Long id) throws IOException{
			ResponseEntity<CardResponseRest> response = cardService.update(card, id);
			return response;
			
		}
		
		
	
}
