package com.bankapp.mybank.Repository;

import com.bankapp.mybank.Model.DepositInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepositInfoRepository extends JpaRepository<DepositInfo, Long> {
    public DepositInfo findDepositInfoByDepositId(Long id);
    public List<DepositInfo> findDepositInfoByActive(Boolean active);
}
