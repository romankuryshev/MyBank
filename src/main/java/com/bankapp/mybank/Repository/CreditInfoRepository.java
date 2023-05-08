package com.bankapp.mybank.Repository;

import com.bankapp.mybank.Model.CreditInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditInfoRepository extends JpaRepository<CreditInfo, Long> {
}
