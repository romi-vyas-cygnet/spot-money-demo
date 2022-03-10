package com.spotmoney.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spotmoney.domain.Card;
import com.spotmoney.model.request.CreateCardRequest;
import com.spotmoney.model.request.RequestedCards;
import com.spotmoney.model.response.*;
import com.spotmoney.model.response.Errors;
import com.spotmoney.repository.CardRepository;
import com.spotmoney.util.CommonUtils;
import com.spotmoney.web.BinListRestCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Card service to
 * create card,
 * get card list for specific customer,
 * get specific card details
 */
@Service
public class CardService {

    private static final Logger logger = LoggerFactory.getLogger(CardService.class);

    private final CardRepository cardRepository;
    private final CommonUtils commonUtils;
    private final RestTemplate restTemplate;
    private final BinListRestCall client;

    public CardService(CardRepository cardRepository,
                       CommonUtils commonUtils,
                       RestTemplate restTemplate,
                       BinListRestCall client){
        this.cardRepository = cardRepository;
        this.commonUtils = commonUtils;
        this.restTemplate = restTemplate;
        this.client = client;
    }

    /**
     * @param customerId
     * @param createCardRequest
     * @return CreateCardResponse
     * First validates for duplicate card in database
     * Second rest call to binlist api to get country code from json response
     * validate if card comes under banned country list
     * Finally store in database
     */
    public ResponseEntity<CreateCardResponse> createCards(Long customerId, CreateCardRequest createCardRequest) {

        logger.info("Inside create card service for customer {}:",customerId);

        ErrorOrSuccess<CreateCards> createCards
                = checkForDuplicateCardInDB(createCardRequest)
                .applyIfSuccess(cardList -> getCountryCodeForCard(cardList.getCards()))
                .applyIfSuccess(cardCountryCode -> validateIfCardBelongsToBannedCountry(cardCountryCode.getCountryCard()))
                .applyIfSuccess(saveCards -> storeCardList(customerId, saveCards));

        if(createCards.isSuccess()){
             List<String> savedCardList = createCards.getSuccess().getCountryCard().stream()
                     .map(countryCard -> countryCard.getCard()).collect(Collectors.toList());
             return new ResponseEntity<>(new CreateCardResponse(savedCardList), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(new CreateCardResponse(createCards.getError().getError()),
                createCards.getError().getHttpStatus());
    }

    private ErrorOrSuccess<CreateCards> storeCardList(Long customerId, CreateCards saveCards) {
        logger.info("Inside store card list");
        try {
            List<Card> cardList = saveCards.getCountryCard().stream()
                    .map(countryCard -> convertToCard(countryCard.getCard(), customerId))
                    .collect(Collectors.toList());

            cardRepository.saveAll(cardList);
            return ErrorOrSuccess.success(saveCards);
        }catch (Exception ex){
            logger.error("Error occurred while storing cards in db {}:", ex.getMessage());
            return ErrorOrSuccess.error(buildError(new Errors("Error occurred while storing data in database"), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    private ErrorOrSuccess<CreateCards> checkForDuplicateCardInDB(CreateCardRequest createCardRequest) {

        logger.info("Inside checkForDuplicateCardInDB");
        try {
            //prepare list of card in request
            Set<String> inputSet = getCardNumberList(createCardRequest);

            //check in db if card already exist
            Set<Card> dbCardSet = cardRepository.findAllByCardNumberIn(inputSet);

            Set<RequestedCards> duplicateCardsSet = convertToRequestedCardSet(dbCardSet);

            logger.info("Total duplicates found :{}", duplicateCardsSet.size());

            //remove duplicate
            Set<RequestedCards> finalCards = createCardRequest.getCards().stream()
                    .filter(requestCard -> !duplicateCardsSet.contains(requestCard)).collect(Collectors.toSet());

            //check if set is not empty
            if(finalCards.size() == 0){
                logger.error("No cards left to be processed after removing duplicates");
                return ErrorOrSuccess.error(buildError(new Errors("Card/s received for process were duplicate"), HttpStatus.BAD_REQUEST));
            }

            Set<String> validatedCards = finalCards.stream()
                    .map(reqCard -> reqCard.getCardNumber()).collect(Collectors.toSet());

            return ErrorOrSuccess.success(CreateCards.builder().cards(validatedCards).build());
        }catch (Exception ex){
            logger.error("Error received while duplicate check in db :{}",ex.getMessage());
            return ErrorOrSuccess.error(buildError(new Errors("Error occurred while checking for duplicates in database"), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    private ErrorOrSuccess<CreateCards> validateIfCardBelongsToBannedCountry(Set<CountryCard> countryCard) {
        logger.info("Inside validate card for banned country");
        try {
            Set<CountryCard> validatedList = countryCard.stream().filter(countryCodeCard -> {
                if (validateCountry(countryCodeCard)) {
                    return true;
                } else {
                    return false;
                }
            }).collect(Collectors.toSet());

            //validate if any card left in list after removing from banned country list
            if (validatedList.size() == 0) {
                logger.error("No cards left to be saved after validation in banned country list");
                return ErrorOrSuccess.error(buildError(new Errors("No cards left to be saved after validation in banned country list"), HttpStatus.BAD_REQUEST));
            }
            return ErrorOrSuccess.success(CreateCards.builder().countryCard(validatedList).build());
        }catch (Exception ex){
            logger.error("Error occurred while validating card in banned country list {}:", ex.getMessage());
            return ErrorOrSuccess.error(buildError(new Errors("Error occurred while validating in banned country list"), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    private ErrorOrSuccess<CreateCards> getCountryCodeForCard(Set<String> cards) {
        logger.info("Inside get country code");
        try {
            Set<CountryCard> set = cards.
                    stream().map(card -> {
                        try {
                            return getCountryCode(card);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException();
                        }
                    }).collect(Collectors.toSet());
            return  ErrorOrSuccess.success(CreateCards.builder().countryCard(set).build());
        }catch (Exception ex){
            logger.error("Error occurred while getting country code {}",ex.getMessage());
            return ErrorOrSuccess.error(buildError(new Errors("Error occurred while making rest call to get country code"), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    private Set<RequestedCards> convertToRequestedCardSet(Set<Card> cardList) {
        return cardList.stream().map(card -> convertList(card.getCardNumber())).collect(Collectors.toSet());
    }

    private Set<String> getCardNumberList(CreateCardRequest createCardRequest) {
        return createCardRequest.getCards().stream().map(requestedCard -> requestedCard.getCardNumber()).collect(Collectors.toSet());
    }

    private RequestedCards convertList(String cardNumber){
        return RequestedCards.builder().cardNumber(cardNumber).build();
    }

    private CountryCard getCountryCode(String card) throws JsonProcessingException {
        return client.getCountryCode(card);
    }

    private Card convertToCard(String card, Long customerId) {
        return Card.builder().customerId(customerId).cardNumber(card).status(0).build();
    }

    private boolean validateCountry(CountryCard countryCard) {
         return Objects.isNull(commonUtils.getCountry(countryCard.getCountryCode()));
    }

    /**
     *
     * @param customerId
     * @return GetCardListResponse
     * Gets all cards for specific customer
     */
    public ResponseEntity<GetCardListResponse> getCards(Long customerId) {
        logger.info("Inside get card list service for cutomer {}:",customerId);
        ErrorOrSuccess<GetCardList> getCardList
                = getCardList(customerId);

        if(getCardList.isSuccess()){
            List<CardList> cardList = ignoreCustomerIdInList(getCardList.getSuccess().getCards());
            return new ResponseEntity<>(new GetCardListResponse(customerId, cardList),
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(new GetCardListResponse(customerId, getCardList.getError().getError()),
                getCardList.getError().getHttpStatus());
    }

    private List<CardList> ignoreCustomerIdInList(List<Card> cards) {
       return cards.stream().map(card -> CardList.builder().cardId(card.getCardId()).cardNumber(card.getCardNumber()).status(card.getStatus()).createdAt(card.getCreatedAt()).build()).collect(Collectors.toList());
    }

    private ErrorOrSuccess<GetCardList> getCardList(Long customerId) {
        Optional<List<Card>> cardList = cardRepository.findByCustomerId(customerId);
        if(cardList.isEmpty() || cardList.get().size() == 0){
            logger.error("No cards found for given customer {}:",customerId);
            return ErrorOrSuccess.error(buildError(new Errors("Card details not found for given customer"), HttpStatus.NOT_FOUND));
        }
        return ErrorOrSuccess.success(new GetCardList(cardList.get()));
    }

    private SpotMoneyDemoError buildError(Errors error, HttpStatus statusCode) {
        return SpotMoneyDemoError.builder().error(error).httpStatus(statusCode).build();
    }

    /**
     *
     * @param customerId
     * @param cardId
     * @return
     * Gets specific card detail
     */
    public ResponseEntity<GetCardResponse> getCardResponse(Long customerId, Long cardId) {
        logger.info("Inside get card service for cutomer {}: card {}:",customerId, cardId);
        ErrorOrSuccess<GetCard> getCard
                = getCard(customerId, cardId);

        if(getCard.isSuccess()){
            return new ResponseEntity<>(new GetCardResponse(getCard.getSuccess().getCard()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new GetCardResponse(getCard.getError().getError()),
                getCard.getError().getHttpStatus());
    }

    private ErrorOrSuccess<GetCard> getCard(Long customerId, Long cardId) {
        Optional<Card> card = cardRepository.findByCustomerIdAndCardId(customerId, cardId);
        if(!card.isPresent()){
            logger.error("Card details not found for card number",cardId);

            return ErrorOrSuccess.error(buildError(new Errors("Card details not found for given card number"), HttpStatus.NOT_FOUND));
        }
        return ErrorOrSuccess.success(new GetCard(card.get()));
    }
}