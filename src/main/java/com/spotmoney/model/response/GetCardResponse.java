package com.spotmoney.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spotmoney.domain.Card;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * Get card response class
 */
@Value
@NoArgsConstructor(force = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetCardResponse {

    Card card;
    Errors error;

    public GetCardResponse(Card card, Errors error){
        this.card = card;
        this.error = error;
    }

    public GetCardResponse(Card card){
        this(card, null);
    }

    public GetCardResponse(Errors error){
        this(null, error);
    }
}
