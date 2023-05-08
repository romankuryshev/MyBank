package com.bankapp.mybank.Service;

import com.bankapp.mybank.Model.UpdateInfo;
import com.bankapp.mybank.Model.UpdateType;
import com.bankapp.mybank.Repository.CardsRepository;
import com.bankapp.mybank.Repository.DepositRepository;
import com.bankapp.mybank.Repository.UpdateInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.Locale;

@Service
public class AdminService {
    private final UpdateInfoRepository updateInfoRepository;
    private final CardsRepository cardsRepository;
    private final DepositRepository depositRepository;

    @Autowired
    public AdminService(UpdateInfoRepository updateInfoRepository, CardsRepository cardsRepository, DepositRepository depositRepository) {
        this.updateInfoRepository = updateInfoRepository;
        this.cardsRepository = cardsRepository;
        this.depositRepository = depositRepository;
    }

    public void doUpdate(UpdateType updateType){
        UpdateInfo lastUpdate = updateInfoRepository.getUpdateInfoByUpdateType(updateType);
        LocalDate today = LocalDate.now();
        DecimalFormat decimalFormat = new DecimalFormat( "#.##", new DecimalFormatSymbols(Locale.US));
//        lastUpdate.setUpdateDate(LocalDate.now().minusDays(1));
        if(lastUpdate == null || lastUpdate.getUpdateDate().isBefore(today)){
            switch (updateType) {
                case ALL -> {
                    updateDeposits(today, decimalFormat);
                    updateCards(today, decimalFormat);
                }

                case CARD -> updateCards(today, decimalFormat);
                case DEPOSIT -> updateDeposits(today, decimalFormat);

                default -> {
                }
            }

            if(lastUpdate == null)
                lastUpdate = new UpdateInfo(updateType, today);
            else lastUpdate.setUpdateDate(today);
            updateInfoRepository.save(lastUpdate);
        }
        if(updateType == UpdateType.ALL){
            for(var update : updateInfoRepository.findAll())
                update.setUpdateDate(today);
        }
    }

    public LocalDate getLastUpdate(UpdateType updateType){
        UpdateInfo info = updateInfoRepository.getUpdateInfoByUpdateType(updateType);
        if(info != null)
            return info.getUpdateDate();
        else return null;
    }
    public void updateDeposits(LocalDate today, DecimalFormat decimalFormat){
        for(var deposit : depositRepository.findAll()){
            if(!deposit.isActive())
                continue;

            deposit.setNextUpdateDate(today);
            if(deposit.getEndDate().isEqual(today) || deposit.getEndDate().isBefore(today)){
                deposit.setActive(false);
            }
            else if(deposit.getNextUpdateDate().isEqual(today) || deposit.getNextUpdateDate().isBefore(today)){
                Double newBalance = deposit.getBalance() + deposit.getMinBalance() * (deposit.getPercent() * 0.01 / 12);
                newBalance = Double.parseDouble(decimalFormat.format(newBalance));
                deposit.setBalance(newBalance);
                deposit.setMinBalance(deposit.getBalance());
                deposit.setNextUpdateDate(today.plusMonths(1));
            }

            depositRepository.save(deposit);
        }
    }

    public void updateCards(LocalDate today, DecimalFormat decimalFormat){
        for(var card : cardsRepository.findAll()) {

            if(!card.getActive())
                continue;

            if(card.getEndDate().isEqual(today) || card.getEndDate().isBefore(today)) {
                card.setActive(false);
            } else if (card.getNextCashbackUpdate().isEqual(today) || card.getNextCashbackUpdate().isBefore(today)) {
                Double newBalance = card.getBalance() + card.getSpendingForMonth() * (card.getCashbackPercent() * 0.01 / 12);
                newBalance = Double.parseDouble(decimalFormat.format(newBalance));
                card.setNextCashbackUpdate(today.plusMonths(1));
                card.setBalance(newBalance);
                card.setSpendingForMonth(0.0);
            }

            cardsRepository.save(card);
        }
    }
}
