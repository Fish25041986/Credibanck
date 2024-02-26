package com.credibanco.tarjeta.response;

import java.util.List;

import com.credibanco.tarjeta.model.Transaction;

import lombok.Data;

@Data
public class TransactionResponse {
	
	private List<Transaction> transactional;

}
