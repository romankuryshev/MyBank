package com.bankapp.mybank.Repository;

import com.bankapp.mybank.Model.Deposit;
import com.bankapp.mybank.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepositRepository extends JpaRepository<Deposit, Long> {
    List<Deposit> findAllByUser(User user);
    Deposit findByDepId(Long depId);
}
