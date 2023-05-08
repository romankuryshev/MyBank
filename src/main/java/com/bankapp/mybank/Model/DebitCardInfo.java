package com.bankapp.mybank.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class DebitCardInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer cardId;
    private String name;
    private String description;
    private Double price;
    private Double cashBackPercent;
    private Double operationsLimit;

    public DebitCardInfo(){

    }

    public DebitCardInfo(String name, String description, Double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCashBackPercent() {
        return cashBackPercent;
    }

    public void setCashBackPercent(Double cashBackPercent) {
        this.cashBackPercent = cashBackPercent;
    }

    public Double getOperationsLimit() {
        return operationsLimit;
    }

    public void setOperationsLimit(Double operationsLimit) {
        this.operationsLimit = operationsLimit;
    }
}
