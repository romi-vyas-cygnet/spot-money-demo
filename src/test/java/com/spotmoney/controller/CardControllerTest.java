package com.spotmoney.controller;

import com.spotmoney.model.response.CreateCardResponse;
import com.spotmoney.model.response.GetCardListResponse;
import com.spotmoney.model.response.GetCardResponse;
import com.spotmoney.service.CardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;

import static com.spotmoney.util.InstanceHelper.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardControllerTest {

    @InjectMocks
    CardController cardController;

    @Mock
    CardService cardService;

    @Test
    void create_card_success_test() {
        //given
        when(cardService.createCards(any(Long.class), any())).thenReturn(getCreateCardResponse());

        //when
        ResponseEntity<CreateCardResponse> response = cardController.createCards(CUSTOMER_ID, createCardRequest(), new BeanPropertyBindingResult(new Object(), "abc"));

        //then
        assertNotNull(response.getBody().getStoredCardList());
        verify(cardService, times(1)).createCards(any(Long.class), any());
    }

    @Test
    void create_card_error_test() {
        //given
        doThrow(new RuntimeException()).when(cardService).createCards(any(Long.class), any());

        //when
        ResponseEntity<CreateCardResponse> response = cardController.createCards(CUSTOMER_ID, createCardRequest(), new BeanPropertyBindingResult(new Object(), "abc"));

        //then
        assertNotNull(response.getBody().getError());
        verify(cardService, times(1)).createCards(any(Long.class), any());
    }

    @Test
    void get_card_list_response_success_test() {
        //given
        when(cardService.getCards(any(Long.class))).thenReturn(getCardListResponse());

        //when
        ResponseEntity<GetCardListResponse> response = cardController.getCardList(CUSTOMER_ID);

        //then
        assertNotNull(response.getBody().getCards());
        verify(cardService, times(1)).getCards(any(Long.class));
    }

    @Test
    void get_card_list_response_error_test() {
        //given
        doThrow(new RuntimeException()).when(cardService).getCards(any(Long.class));

        //when
        ResponseEntity<GetCardListResponse> response = cardController.getCardList(CUSTOMER_ID);

        //then
        assertNull(response.getBody().getCards());
        verify(cardService, times(1)).getCards(any(Long.class));
    }

    @Test
    void get_card_response_success_test() {
        //given
        when(cardService.getCardResponse(any(Long.class), any(Long.class))).thenReturn(getCardResponse());

        //when
        ResponseEntity<GetCardResponse> response = cardController.getCardResponse(CUSTOMER_ID,CARD_ID);

        //then
        assertNotNull(response.getBody().getCard());
        verify(cardService, times(1)).getCardResponse(any(Long.class), any(Long.class));
    }

    @Test
    void get_card_response_error_test() {

        //given
        doThrow(new RuntimeException()).when(cardService).getCardResponse(any(Long.class), any(Long.class));

        //when
        ResponseEntity<GetCardResponse> response = cardController.getCardResponse(CUSTOMER_ID,CARD_ID);

        //then
        assertNull(response.getBody().getCard());
        verify(cardService, times(1)).getCardResponse(any(Long.class), any(Long.class));
    }
}
