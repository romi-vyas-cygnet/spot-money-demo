package com.spotmoney.repository;

import com.spotmoney.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<List<Card>> findByCustomerId(Long customerId);

    Optional<Card> findByCustomerIdAndCardId(Long customerId, Long cardNumber);

    Set<Card> findAllByCardNumberIn(@Param("cards") Set<String> cards);

    Set<Card> findTop5ByStatusOrderByCreatedAt(int i);

    @Modifying
    @Query(value = "UPDATE Card c SET c.status=:cardStatus WHERE c.cardId IN (:cards)")
    @Transactional
    int updateStatus(@Param("cardStatus") int cardStatus,@Param("cards") Set<Long> cards);
}
