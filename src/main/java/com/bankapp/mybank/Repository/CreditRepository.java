package com.bankapp.mybank.Repository;

import com.bankapp.mybank.Model.Credit;
import com.bankapp.mybank.Model.DebitCard;
import com.bankapp.mybank.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditRepository extends JpaRepository<Credit, Long> {
    public Credit findCreditByCredId(Long credId);
    public List<Credit> findAllCreditsByUser(User user);
    Credit findCreditByDebitCard(DebitCard card);
}
