package com.spotmoney.controller;

import com.spotmoney.enums.ResponseInfo;
import com.spotmoney.model.request.CreateCardRequest;
import com.spotmoney.model.response.CreateCardResponse;
import com.spotmoney.model.response.Errors;
import com.spotmoney.model.response.GetCardListResponse;
import com.spotmoney.model.response.GetCardResponse;
import com.spotmoney.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Card Controller
 */
@RestController
@RequestMapping(value = "api/vi/customer/{customerId}/cards")
public class CardController {

    private static final Logger logger = LoggerFactory.getLogger(CardController.class);

    private final CardService cardService;

    public CardController(CardService cardService){
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<CreateCardResponse> createCards(@PathVariable Long customerId,
                                                          @Valid @RequestBody CreateCardRequest createCardRequest,
                                                          BindingResult result){
        logger.info("Inside create card controller for customer :{}",customerId);
        if(result.hasErrors()){
            logger.info("Invalid create card request");
            return new ResponseEntity<>(new CreateCardResponse(buildIvalidRequestErrorResponse()), HttpStatus.BAD_REQUEST);
        }
        try {
            return cardService.createCards(customerId, createCardRequest);
        }catch (Exception ex){
            logger.error("Error received while processing create card :{}",ex.getMessage());
            return new ResponseEntity<>(new CreateCardResponse(buildGenericErrorResponse()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Errors buildIvalidRequestErrorResponse() {
        return new Errors(ResponseInfo.INVALID_REQUEST_ERROR.getValue());
    }

    @GetMapping
    public ResponseEntity<GetCardListResponse> getCardList(@PathVariable Long customerId){
        logger.info("Inside get card list controller for customer :{}",customerId);
        try{
            return cardService.getCards(customerId);
        }catch (Exception ex){
            logger.error("Error received while processing get card list :{}",ex.getMessage());
            return new ResponseEntity<>(new GetCardListResponse(customerId, buildGenericErrorResponse()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Errors buildGenericErrorResponse() {
        return new Errors(ResponseInfo.INTERNAL_SERVER_ERROR.getValue());
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<GetCardResponse> getCardResponse(@PathVariable Long customerId,
                                                           @PathVariable Long cardId){
        logger.info("Inside get card controller for customer :{} card :{} ", customerId, cardId);
        try{
            return cardService.getCardResponse(customerId, cardId);
        }catch (Exception ex){
            logger.error("Error received while processing get card {}:",ex.getMessage());
            return new ResponseEntity<>(new GetCardResponse(buildGenericErrorResponse()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
