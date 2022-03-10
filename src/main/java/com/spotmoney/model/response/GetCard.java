package com.spotmoney.model.response;

import com.spotmoney.domain.Card;
import lombok.NoArgsConstructor;
import lombok.Value;


@Value
@NoArgsConstructor(force = true)
public class GetCard {
    Card card;

    public GetCard(Card card){
        this.card = card;
    }
}
