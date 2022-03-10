package com.spotmoney.schedular;

import com.spotmoney.domain.Card;
import com.spotmoney.repository.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Schedular that will get card whose status is 0 every 30 sec and updates its status to 1
 */
@Component
public class UpdateStatusSchedular {

    private static final Logger logger = LoggerFactory.getLogger(UpdateStatusSchedular.class);

    private final CardRepository cardRepository;

    public UpdateStatusSchedular(CardRepository cardRepository){
         this.cardRepository = cardRepository;
    }

    @Scheduled(initialDelay = 30000, fixedDelay = 30000)
    public void updateStatus(){
        logger.info("Welcome to update status {}:", new Date());

        Set<Card> cardList = cardRepository.findTop5ByStatusOrderByCreatedAt(0);

        Set<Long> cardIds = cardList.stream().map(card -> card.getCardId()).collect(Collectors.toSet());

        int affectedRows = cardRepository.updateStatus(1, cardIds);
    }
}
