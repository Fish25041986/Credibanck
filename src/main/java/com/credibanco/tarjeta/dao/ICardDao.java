package com.credibanco.tarjeta.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.credibanco.tarjeta.model.Card;

public interface ICardDao extends JpaRepository<Card, Long>{
	
	 Optional<Card> findByCardNumber(String cardNumber);

}
