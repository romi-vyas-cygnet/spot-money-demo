package com.spotmoney.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

/**
 * Displays list of card numbers that were stored
 */
@Value
@NoArgsConstructor(force = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateCardResponse {

    List<String> storedCardList;
    Errors error;

    public CreateCardResponse(@JsonProperty("storedCardList") List<String> storedCardList,
                              @JsonProperty("error") Errors error){
        this.storedCardList = storedCardList;
        this.error = error;
    }

    public CreateCardResponse(List<String> storedCardList){
        this(storedCardList, null);
    }

    public CreateCardResponse(Errors error){
        this(null, error);
    }
}
