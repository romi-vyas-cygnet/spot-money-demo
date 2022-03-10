package com.spotmoney.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseInfo {

    INTERNAL_SERVER_ERROR("Error occurred at server while processing request"),
    INVALID_REQUEST_ERROR("Invalid request parameters");

    private final String value;
}
