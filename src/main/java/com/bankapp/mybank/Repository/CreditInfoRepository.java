package com.bankapp.mybank.Repository;

import com.bankapp.mybank.Model.CreditInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditInfoRepository extends JpaRepository<CreditInfo, Long> {
    public CreditInfo findCreditInfoByCreditId(Long creditId);
    public List<CreditInfo> findCreditInfoByActive(Boolean active);
}
