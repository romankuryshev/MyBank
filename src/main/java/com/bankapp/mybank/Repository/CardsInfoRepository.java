package com.bankapp.mybank.Repository;

import com.bankapp.mybank.Model.DebitCardInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardsInfoRepository extends JpaRepository<DebitCardInfo, Integer> {
    DebitCardInfo findDebitCardInfoByCardId(Integer cardId);
}
