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

    private Integer minSum;
    private Integer maxSum;
    private String description;
    private String name;
    private Boolean active;

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

    public Integer getMinSum() {
        return minSum;
    }

    public void setMinSum(Integer minSum) {
        this.minSum = minSum;
    }

    public Integer getMaxSum() {
        return maxSum;
    }

    public void setMaxSum(Integer maxSum) {
        this.maxSum = maxSum;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
