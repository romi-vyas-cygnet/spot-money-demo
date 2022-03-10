package com.spotmoney.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

/**
 * Get card list response class
 */
@Value
@NoArgsConstructor(force = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetCardListResponse {

    Long customerId;
    List<CardList> cards;
    Errors error;

    public GetCardListResponse(Long customerId, List<CardList> cards, Errors error){
        this.customerId = customerId;
        this.cards = cards;
        this.error = error;
    }

    public GetCardListResponse(Long customerId,List<CardList> cards){
        this(customerId, cards, null);
    }

    public GetCardListResponse(Long customerId, Errors error){
        this(customerId,null, error);
    }
}
