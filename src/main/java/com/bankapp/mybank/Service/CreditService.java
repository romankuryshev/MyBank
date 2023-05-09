package com.bankapp.mybank.Service;

import com.bankapp.mybank.Enums.CreditStatementType;
import com.bankapp.mybank.Model.*;
import com.bankapp.mybank.Repository.CardsRepository;
import com.bankapp.mybank.Repository.CreditInfoRepository;
import com.bankapp.mybank.Repository.CreditRepository;
import com.bankapp.mybank.Repository.CreditStatementRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CreditService {

    private final CreditInfoRepository creditInfoRepository;
    private final CreditRepository creditRepository;
    private final CardsRepository cardsRepository;
    private final CreditStatementRepository creditStatementRepository;

    @Autowired
    public CreditService(CreditInfoRepository creditInfoRepository, CreditRepository creditRepository, CardsRepository cardsRepository, CreditStatementRepository creditStatementRepository) {
        this.creditStatementRepository = creditStatementRepository;
        this.creditRepository = creditRepository;
        this.creditInfoRepository = creditInfoRepository;
        this.cardsRepository = cardsRepository;
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


    public String addCreditStatement(Long creditId,
                                     User user,
                                     String cardNumber,
                                     Double sum,
                                     Integer period){

        DebitCard debitCard = cardsRepository.findByCardNumber(cardNumber);
        CreditInfo creditInfo = creditInfoRepository.findCreditInfoByCreditId(creditId);

        Credit newCredit = new Credit(creditInfo, user, debitCard, sum);
        creditRepository.save(newCredit);

        CreditStatement creditStatement = new CreditStatement(newCredit, CreditStatementType.EXPECTS, period);
        creditStatementRepository.save(creditStatement);
        return "Заявка отправлена!";
    }
}
