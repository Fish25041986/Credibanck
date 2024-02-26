package com.credibanco.tarjeta.utils;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class CardNumberGenerator {
	
	    private static final SecureRandom random = new SecureRandom();
	    private static final int TOTAL_DIGITS = 16;
	    private static final int PRODUCT_ID_LENGTH = 6;

	    /**
	     * Generates a 16-digit card number using the productId as the first 6 digits.
	     * @param productId The ID of the product that will be part of the card number.
	     * @return A card number as a String.
	     */
	    public static String generateCardNumber(long productId) {
	        // Ensures that the ID has six digits, in case it does not have the
	        String productIdString = String.format("%0" + PRODUCT_ID_LENGTH + "d", productId);

	        //generate the remaining ten numbers randomly
	        StringBuilder cardNumberBuilder = new StringBuilder(productIdString);
	        for (int i = productIdString.length(); i < TOTAL_DIGITS; i++) {
	            int digit = random.nextInt(10);
	            cardNumberBuilder.append(digit);
	        }

	        return cardNumberBuilder.toString();
	    }
}
