package com.bankapp.mybank.Repository;

import com.bankapp.mybank.Enums.CreditStatementType;
import com.bankapp.mybank.Model.CreditStatement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditStatementRepository extends JpaRepository<CreditStatement, Long> {

    public List<CreditStatement> findCreditStatementsByCreditStatementType(CreditStatementType type);
    public CreditStatement findCreditStatementsByCreditStatementId(Long creditStatementId);
}
