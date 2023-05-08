package com.bankapp.mybank.Service;

import com.bankapp.mybank.Model.CreditInfo;
import com.bankapp.mybank.Repository.CreditInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditService {

    private final CreditInfoRepository creditInfoRepository;

    @Autowired
    public CreditService(CreditInfoRepository creditInfoRepository) {
        this.creditInfoRepository = creditInfoRepository;
    }

    public void addCreditInfo(CreditInfo creditInfo){
        creditInfo.setActive(true);
        creditInfoRepository.save(creditInfo);
    }

    public CreditInfo getCreditInfo(Long creditId){
        return creditInfoRepository.findCreditInfoByCreditId(creditId);
    }
    public List<CreditInfo> getAllCredits(){
        return creditInfoRepository.findCreditInfoByActive(true);
    }

    public void hideCredit(Long creditId){
        CreditInfo credit = creditInfoRepository.findCreditInfoByCreditId(creditId);
        if(credit != null){
            credit.setActive(false);
            creditInfoRepository.save(credit);
        }
    }

    public void updateCredit(Long creditId, CreditInfo creditInfo){
        creditInfo.setActive(true);
        CreditInfo credit = creditInfoRepository.findCreditInfoByCreditId(creditId);
        if(credit != null){
            credit = creditInfo;
            creditInfoRepository.save(credit);
        }
    }
}
