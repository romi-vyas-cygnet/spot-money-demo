package com.spotmoney.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;

/**
 * Generic class for handling errors of all apis
 */
@Value
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class SpotMoneyDemoError {

   Errors error;
   HttpStatus httpStatus;
}
