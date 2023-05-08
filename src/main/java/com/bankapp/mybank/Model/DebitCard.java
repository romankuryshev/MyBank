package com.bankapp.mybank.Model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class DebitCard {

    private static final Integer PERIOD = 4;
    @Id
    private String cardNumber;
    @ManyToOne
    private DebitCardInfo cardInfo;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    private String cvcCode;
    private String password;
    private Double balance;
    private Double spendingForMonth;
    private Double cashbackPercent;
    private LocalDate nextCashbackUpdate;
    private LocalDate endDate;
    private Boolean isActive;

    public DebitCard(){}

    public DebitCard(User user,DebitCardInfo cardInfo, /*String password,*/  String cardNumber, String cvc) {
        this.user = user;
//        this.password = password;
        this.cardInfo = cardInfo;
        this.cvcCode = cvc;
        this.cardNumber = cardNumber;
        isActive = true;
        balance = 0.0;
        spendingForMonth = 0.0;
        endDate = LocalDate.now().plusYears(PERIOD);
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public DebitCardInfo getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(DebitCardInfo cardInfo) {
        this.cardInfo = cardInfo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCvcCode() {
        return cvcCode;
    }

    public void setCvcCode(String cvcCode) {
        this.cvcCode = cvcCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getSpendingForMonth() {
        return spendingForMonth;
    }

    public void setSpendingForMonth(Double spendingForMonth) {
        this.spendingForMonth = spendingForMonth;
    }

    public Double getCashbackPercent() {
        return cashbackPercent;
    }

    public void setCashbackPercent(Double cashbackPercent) {
        this.cashbackPercent = cashbackPercent;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDate getNextCashbackUpdate() {
        return nextCashbackUpdate;
    }

    public void setNextCashbackUpdate(LocalDate nextCashbackUpdate) {
        this.nextCashbackUpdate = nextCashbackUpdate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
