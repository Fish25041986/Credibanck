package com.credibanco.tarjeta.response;

import java.util.List;

import com.credibanco.tarjeta.model.Card;

import lombok.Data;

@Data
public class CardResponse {
	
	private List<Card> card;


}
