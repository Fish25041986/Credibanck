package com.credibanco.tarjeta.utils;

import org.springframework.stereotype.Component;

import com.credibanco.tarjeta.response.TransactionResponseRest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ResponseConverter {

	private static ObjectMapper objectMapper = new ObjectMapper();

    public static String convertTransactionResponseToString(TransactionResponseRest response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); 
            return null;
        }
    }
	
}
