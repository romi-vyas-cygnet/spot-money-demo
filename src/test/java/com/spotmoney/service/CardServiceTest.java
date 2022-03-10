package com.spotmoney.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spotmoney.model.response.CreateCardResponse;
import com.spotmoney.model.response.GetCardListResponse;
import com.spotmoney.model.response.GetCardResponse;
import com.spotmoney.repository.CardRepository;
import com.spotmoney.util.CommonUtils;
import com.spotmoney.web.BinListRestCall;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.Set;

import static com.spotmoney.util.InstanceHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @InjectMocks
    CardService cardService;

    @Mock
    CardRepository cardRepository;

    @Mock
    BinListRestCall client;

    @Mock
    CommonUtils commonUtils;

    @Test
    void create_card_duplicate_error_test() {
        //given
        doThrow(new RuntimeException()).when(cardRepository).findAllByCardNumberIn(any());

        //when
        ResponseEntity<CreateCardResponse> response = cardService.createCards(CUSTOMER_ID, createCardRequest());

        //then
        assertEquals("Error occurred while checking for duplicates in database", response.getBody().getError().getMessage());
        verify(cardRepository, times(1)).findAllByCardNumberIn(any());
    }

    @Test
    void create_card_rest_call_error_test() throws JsonProcessingException {
        //given
        when(cardRepository.findAllByCardNumberIn(any())).thenReturn(Set.of(getCard()));
        doThrow(new RuntimeException()).when(client).getCountryCode(any(String.class));

        //when
        ResponseEntity<CreateCardResponse> response = cardService.createCards(CUSTOMER_ID, createCardRequest());

        //then
        assertEquals("Error occurred while making rest call to get country code", response.getBody().getError().getMessage());
        verify(cardRepository, times(1)).findAllByCardNumberIn(any());
    }

    @Test
    void create_card_banned_error_test() throws JsonProcessingException {
        //given
        when(cardRepository.findAllByCardNumberIn(any())).thenReturn(Set.of(getCard()));
        when(client.getCountryCode(any(String.class))).thenReturn(getCountryCard());
        doThrow(new RuntimeException()).when(commonUtils).getCountry(any(String.class));

        //when
        ResponseEntity<CreateCardResponse> response = cardService.createCards(CUSTOMER_ID, createCardRequest());

        //then
        assertEquals("Error occurred while validating in banned country list", response.getBody().getError().getMessage());
        verify(cardRepository, times(1)).findAllByCardNumberIn(any());
    }

    @Test
    void create_card_success_test() throws JsonProcessingException {
        //given
        when(cardRepository.findAllByCardNumberIn(any())).thenReturn(Set.of(getCard()));
        when(client.getCountryCode(any(String.class))).thenReturn(getCountryCard());
        when(commonUtils.getCountry(any(String.class))).thenReturn(null);

        //when
        ResponseEntity<CreateCardResponse> response = cardService.createCards(CUSTOMER_ID, createCardRequest());

        //then
        assertNull(response.getBody().getError());
        verify(cardRepository, times(1)).findAllByCardNumberIn(any());
    }

	@Test
	void get_card_list_response_error_test() {

        //given
        when(cardRepository.findByCustomerId(any(Long.class))).thenReturn(Optional.empty());

        //when
        ResponseEntity<GetCardListResponse> response = cardService.getCards(CUSTOMER_ID);

        //then
        assertEquals(GET_CARD_LIST_ERROR_DESC, response.getBody().getError().getMessage());
        verify(cardRepository, times(1)).findByCustomerId(any(Long.class));
	}

    @Test
    void get_card_list_response_success_test() {

        //given
        when(cardRepository.findByCustomerId(any(Long.class))).thenReturn(Optional.of(getCardList()));

        //when
        ResponseEntity<GetCardListResponse> response = cardService.getCards(CUSTOMER_ID);

        //then
        assertNull(response.getBody().getError());
        verify(cardRepository, times(1)).findByCustomerId(any(Long.class));
    }

    @Test
    void get_card_response_error_test() {

        //given
        when(cardRepository.findByCustomerIdAndCardId(any(Long.class), any(Long.class))).thenReturn(Optional.empty());

        //when
        ResponseEntity<GetCardResponse> response = cardService.getCardResponse(CUSTOMER_ID, CARD_ID);

        //then
        assertEquals(GET_CARD_ERROR_DESC, response.getBody().getError().getMessage());
        verify(cardRepository, times(1)).findByCustomerIdAndCardId(any(Long.class), any(Long.class));
    }

    @Test
    void get_card_response_success_test() {

        //given
        when(cardRepository.findByCustomerIdAndCardId(any(Long.class), any(Long.class))).thenReturn(Optional.of(getCard()));

        //when
        ResponseEntity<GetCardResponse> response = cardService.getCardResponse(CUSTOMER_ID, CARD_ID);

        //then
        assertNull(response.getBody().getError());
        verify(cardRepository, times(1)).findByCustomerIdAndCardId(any(Long.class), any(Long.class));
    }
}
