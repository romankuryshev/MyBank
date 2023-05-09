package com.bankapp.mybank.Repository;

import com.bankapp.mybank.Model.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRepository extends JpaRepository<Credit, Long> {
    public Credit findCreditByCredId(Long credId);
}
