package com.spotmoney.util;

import com.spotmoney.domain.Card;
import com.spotmoney.model.request.CreateCardRequest;
import com.spotmoney.model.request.RequestedCards;
import com.spotmoney.model.response.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public class InstanceHelper {

    public static final String GET_CARD_LIST_ERROR_DESC = "Card details not found for given customer";
    public static final String GET_CARD_ERROR_DESC = "Card details not found for given card number";
    public static final Long CUSTOMER_ID = 1L;
    public static final Long CARD_ID = 1L;
    public static final String CARD_NUM = "1234";
    public static final String CREATE_CARD_NUM = "5678";

    private static CardList cardListBuilder(){
        return CardList.builder().cardId(CARD_ID).cardNumber(CARD_NUM).build();
    }

    private static Card cardBuilder(){
        return Card.builder().cardId(CARD_ID).cardNumber(CARD_NUM).customerId(CUSTOMER_ID).build();
    }
    public static List<Card> getCardList(){
        return List.of(cardBuilder());
    }

    public static Card getCard(){
        return cardBuilder();
    }

    public static ResponseEntity<GetCardListResponse> getCardListResponse(){
        return new ResponseEntity<>(new GetCardListResponse(CUSTOMER_ID, List.of(cardListBuilder())), HttpStatus.OK);
    }

    public static ResponseEntity<GetCardResponse> getCardResponse(){
        return new ResponseEntity<>(new GetCardResponse(cardBuilder()), HttpStatus.OK);
    }

    public static CreateCardRequest createCardRequest(){
        return new CreateCardRequest(Set.of(createRequestedCard()));
    }

    public static RequestedCards createRequestedCard(){
        return RequestedCards.builder().cardNumber(CREATE_CARD_NUM).build();
    }

    public static ResponseEntity<CreateCardResponse> getCreateCardResponse(){
        return new ResponseEntity<>(new CreateCardResponse(List.of(CARD_NUM)), HttpStatus.CREATED);
    }

    public static CountryCard getCountryCard(){
        return CountryCard.builder().countryCode("355").card(CREATE_CARD_NUM).build();
    }
}
