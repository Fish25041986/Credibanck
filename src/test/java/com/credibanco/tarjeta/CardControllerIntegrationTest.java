package com.credibanco.tarjeta;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.credibanco.tarjeta.controller.CardRestController;
import com.credibanco.tarjeta.model.Card;
import com.credibanco.tarjeta.response.CardResponseRest;
import com.credibanco.tarjeta.services.CardServiceImplement;


@WebMvcTest(CardRestController.class)
public class CardControllerIntegrationTest {
	
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardServiceImplement cardService;

    @Test
    public void testActivateCard() throws Exception {
        String cardNumber = "1234567890123456";
        Card mockCard = new Card();
        mockCard.setCardNumber(cardNumber);
        CardResponseRest mockResponse = new CardResponseRest();
        mockResponse.getCardResponse().setCard(Collections.singletonList(mockCard));

        Mockito.when(cardService.searchBynumber(cardNumber)).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        mockMvc.perform(post("/cards/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"cardId\":\"" + cardNumber + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardResponse.card[0].cardNumber", is(cardNumber)));
    }
}
