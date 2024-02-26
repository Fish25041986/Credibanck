package com.credibanco.tarjeta.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CardDto {
	
	private String cardId;
	private BigDecimal balance;



}
