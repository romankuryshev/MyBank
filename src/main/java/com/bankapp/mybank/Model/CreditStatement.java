package com.bankapp.mybank.Model;

import com.bankapp.mybank.Enums.CreditStatementType;
import jakarta.persistence.*;

@Entity
public class CreditStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long creditStatementId;

    @OneToOne
    @JoinColumn(name = "cred_id")
    private Credit credit;
    private CreditStatementType creditStatementType;
    private Integer period;

    public CreditStatement(){

    }

    public CreditStatement(Credit credit, CreditStatementType creditStatementType, Integer period) {
        this.credit = credit;
        this.creditStatementType = creditStatementType;
        this.period = period;
    }

    public Long getCreditStatementId() {
        return creditStatementId;
    }

    public void setCreditStatementId(Long creditStatementId) {
        this.creditStatementId = creditStatementId;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public CreditStatementType getCreditStatementType() {
        return creditStatementType;
    }

    public void setCreditStatementType(CreditStatementType creditStatementType) {
        this.creditStatementType = creditStatementType;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }
}
