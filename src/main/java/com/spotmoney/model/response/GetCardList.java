package com.spotmoney.model.response;

import com.spotmoney.domain.Card;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

/**
 * Hold card list
 */
@Value
@NoArgsConstructor(force = true)
public class GetCardList {

    List<Card> cards;

    public GetCardList(List<Card> cards){
        this.cards = cards;
    }
}
