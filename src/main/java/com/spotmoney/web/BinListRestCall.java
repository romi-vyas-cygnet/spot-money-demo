package com.spotmoney.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotmoney.config.AppConstants;
import com.spotmoney.model.response.CountryCard;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


/**
 *  Calls binlist api
 */
@Component
public class BinListRestCall {

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(BinListRestCall.class);

    public BinListRestCall(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public CountryCard getCountryCode(String createCardRequest) throws JsonProcessingException {
            logger.info("Inside get country code for card :{}",createCardRequest);

            ResponseEntity<?> response = restTemplate.getForEntity(AppConstants.URI +createCardRequest.replaceAll("\\s+","").substring(0, 6), Object.class);

            JSONParser parser = new JSONParser();
            Object obj = JSONValue.parse(new ObjectMapper().writeValueAsString(response.getBody()));
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject count = (JSONObject) jsonObject.get("country");
            return CountryCard.builder().card(createCardRequest).countryCode((String) count.get("numeric")).build();
    }
}
