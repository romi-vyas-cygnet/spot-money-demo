package com.spotmoney.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Set;


@Value
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CreateCards {

    Set<String> cards;
    Set<CountryCard> countryCard;
}
