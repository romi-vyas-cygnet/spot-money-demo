package com.spotmoney.model.response;

import lombok.Builder;
import lombok.Value;

/**
 * Holds card number and country code
 */
@Value
@Builder
public class CountryCard {
    String card;
    String countryCode;
}
