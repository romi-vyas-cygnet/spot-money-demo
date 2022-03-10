package com.spotmoney.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * Holds card details
 */
@Value
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CardList {
    Long cardId;
    String cardNumber;
    int status;
    LocalDateTime createdAt;
}
