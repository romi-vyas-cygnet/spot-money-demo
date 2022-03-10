package com.spotmoney.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Card Entity
 */
@Value
@Builder
@Entity
@Table(name = "card_mst")
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "card_id")
    Long cardId;

    @Column(name = "card_number")
    String cardNumber;

    @Column(name = "customer_id")
    Long customerId;

    @Column(name = "status")
    int status;

    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;
}
