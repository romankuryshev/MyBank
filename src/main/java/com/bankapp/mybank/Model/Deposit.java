package com.bankapp.mybank.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.LocalDate;

@Entity
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long depId;
    @ManyToOne
    private DepositInfo depositInfo;

    @ManyToOne
    private User user;
    private LocalDate startDate;
    private LocalDate nextUpdateDate;
    private LocalDate endDate;
    private double balance;
    private double startBalance;
    private double minBalance;
    private boolean isActive;
    private boolean isFirstOpen;
    private  Integer period;
    private Float percent;

    public Deposit(){}

    public Deposit(DepositInfo depositInfo, User user, LocalDate startDate, Integer period) {
        this.depositInfo = depositInfo;
        this.user = user;

        this.period = period;
        this.percent = depositInfo.getPercent();
        this.startDate = startDate;
        this.nextUpdateDate = startDate.plusMonths(1);
        this.endDate = startDate.plusMonths(period);

        isActive = true;
        isFirstOpen = true;
    }

    public Long getDepId() {
        return depId;
    }

    public void setDepId(Long depId) {
        this.depId = depId;
    }

    public DepositInfo getDepositInfo() {
        return depositInfo;
    }

    public void setDepositInfo(DepositInfo depositInfo) {
        this.depositInfo = depositInfo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getNextUpdateDate() {
        return nextUpdateDate;
    }

    public void setNextUpdateDate(LocalDate nextUpdateDate) {
        this.nextUpdateDate = nextUpdateDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getStartBalance() {
        return startBalance;
    }

    public void setStartBalance(double startBalance) {
        this.startBalance = startBalance;
    }

    public double getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(double minBalance) {
        this.minBalance = minBalance;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isFirstOpen() {
        return isFirstOpen;
    }

    public void setFirstOpen(boolean firstOpen) {
        isFirstOpen = firstOpen;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }
}
