package com.credibanco.tarjeta;

import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.credibanco.tarjeta.dao.ICardDao;
import com.credibanco.tarjeta.model.Card;
import com.credibanco.tarjeta.services.CardServiceImplement;

import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CardServiceTest {

    @Mock
    private ICardDao cardDao;

    @InjectMocks
    private CardServiceImplement cardService;

    @Test
    public void testSearchByNumber() {

        String cardNumber = "1234567890123456";
        Card mockCard = new Card();
        mockCard.setCardNumber(cardNumber);
        when(cardDao.findByCardNumber(cardNumber)).thenReturn(Optional.of(mockCard));


        Optional<Card> result = cardDao.findByCardNumber(cardNumber);


        assertTrue(result.isPresent());
        assertEquals(cardNumber, result.get().getCardNumber());
    }
}