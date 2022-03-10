package com.spotmoney.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class RequestedCards {
    @NotBlank(message = "cardNumber is required")
    String cardNumber;
}
