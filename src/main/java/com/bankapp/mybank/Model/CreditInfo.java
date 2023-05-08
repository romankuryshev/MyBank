package com.bankapp.mybank.Model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class CreditInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long creditId;

    private Double creditPercent;

    @ElementCollection(targetClass = Integer.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "credit_terms", joinColumns = @JoinColumn(name = "credit_id"))
    private List<Integer> term;

    @ElementCollection(targetClass = Double.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "creditSums", joinColumns = @JoinColumn(name = "credit_id"))
    private List<Double> sum;

    private String description;
    private String name;

    public Long getCreditId() {
        return creditId;
    }

    public void setCreditId(Long creditId) {
        this.creditId = creditId;
    }

    public Double getCreditPercent() {
        return creditPercent;
    }

    public void setCreditPercent(Double creditPercent) {
        this.creditPercent = creditPercent;
    }

    public List<Integer> getTerm() {
        return term;
    }

    public void setTerm(List<Integer> term) {
        this.term = term;
    }

    public List<Double> getSum() {
        return sum;
    }

    public void setSum(List<Double> sum) {
        this.sum = sum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
