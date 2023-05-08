package com.bankapp.mybank.Service;

import com.bankapp.mybank.Model.Deposit;
import com.bankapp.mybank.Model.DepositInfo;
import com.bankapp.mybank.Model.User;
import com.bankapp.mybank.Repository.DepositInfoRepository;
import com.bankapp.mybank.Repository.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DepositService {
    private final DepositInfoRepository depInfRepo;
    private final DepositRepository depositRepository;

    @Autowired
    public DepositService(DepositInfoRepository depInfRepo, DepositRepository depositRepository) {
        this.depInfRepo = depInfRepo;
        this.depositRepository = depositRepository;
    }

    public List<DepositInfo> getAllDeposits(){
        return depInfRepo.findDepositInfoByActive(true);
    }

    public void addNewDepositInfo(DepositInfo depositInfo){
        depositInfo.setActive(true);
        depInfRepo.save(depositInfo);
    }

    public void hideDeposit(Long depositId){
        DepositInfo deposit = depInfRepo.findDepositInfoByDepositId(depositId);
        deposit.setActive(false);
        depInfRepo.save(deposit);
    }


    public void updateDeposit(Long depositId , DepositInfo depositInfo){
        DepositInfo oldDepositInfo = depInfRepo.findDepositInfoByDepositId(depositId);
        depositInfo.setActive(true);
        oldDepositInfo = depositInfo;
        depInfRepo.save(oldDepositInfo);
    }

    public void addUserDeposit(User user, DepositInfo depositInfo, Integer period){
        Deposit newDeposit = new Deposit(depositInfo, user, LocalDate.now(), period);
        depositRepository.save(newDeposit);
    }
}
