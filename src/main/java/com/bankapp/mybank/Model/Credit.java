package com.bankapp.mybank.Model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long credId;

    @ManyToOne()
    @JoinColumn(name = "credit_id")
    private CreditInfo creditInfo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "card_number")
    private DebitCard debitCard;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nexUpdateDate;
    private Integer period;
    private Double sum;
    private Double nextPayment;
    private Boolean isPaid;

    private Boolean active;
    public Credit() {

    }

    public Credit(CreditInfo creditInfo, User user, DebitCard debitCard, Double sum) {
        this.creditInfo = creditInfo;
        this.user = user;
        this.debitCard = debitCard;
        this.sum = sum;
    }

    public Credit(CreditInfo creditInfo, User user, DebitCard debitCard, LocalDate startDate, LocalDate endDate, LocalDate nexUpdateDate, Double sum, Double nextPayment, Boolean isPaid) {
        this.creditInfo = creditInfo;
        this.user = user;
        this.debitCard = debitCard;
        this.startDate = startDate;
        this.endDate = endDate;
        this.nexUpdateDate = nexUpdateDate;
        this.sum = sum;
        this.nextPayment = nextPayment;
        this.isPaid = isPaid;
    }

    public Long getCredId() {
        return credId;
    }

    public void setCredId(Long credId) {
        this.credId = credId;
    }

    public CreditInfo getCreditInfo() {
        return creditInfo;
    }

    public void setCreditInfo(CreditInfo creditInfo) {
        this.creditInfo = creditInfo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DebitCard getDebitCard() {
        return debitCard;
    }

    public void setDebitCard(DebitCard debitCard) {
        this.debitCard = debitCard;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getNexUpdateDate() {
        return nexUpdateDate;
    }

    public void setNexUpdateDate(LocalDate nexUpdateDate) {
        this.nexUpdateDate = nexUpdateDate;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Double getNextPayment() {
        return nextPayment;
    }

    public void setNextPayment(Double nextPayment) {
        this.nextPayment = nextPayment;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
