package com.spotmoney.model.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CreateCardRequest {

    @Valid
    @NotEmpty(message = "cards are required")
    Set<RequestedCards> cards;
}
