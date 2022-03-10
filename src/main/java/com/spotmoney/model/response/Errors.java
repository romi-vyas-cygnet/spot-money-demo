package com.spotmoney.model.response;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * Class that holds error response for all apis
 */
@Value
@Builder
@RequiredArgsConstructor
public class Errors {
    String message;
}
