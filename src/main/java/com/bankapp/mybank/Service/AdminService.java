package com.bankapp.mybank.Service;

import com.bankapp.mybank.Enums.CreditStatementType;
import com.bankapp.mybank.Model.*;
import com.bankapp.mybank.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
public class AdminService {
    private final UpdateInfoRepository updateInfoRepository;
    private final CardsRepository cardsRepository;
    private final DepositRepository depositRepository;
    private final CreditStatementRepository creditStatementRepository;
    private final CreditRepository creditRepository;

    @Autowired
    public AdminService(UpdateInfoRepository updateInfoRepository, CardsRepository cardsRepository, DepositRepository depositRepository, CreditStatementRepository creditStatementRepository, CreditRepository creditRepository) {
        this.updateInfoRepository = updateInfoRepository;
        this.cardsRepository = cardsRepository;
        this.depositRepository = depositRepository;
        this.creditStatementRepository = creditStatementRepository;
        this.creditRepository = creditRepository;
    }

    public void doUpdate(UpdateType updateType) {
        UpdateInfo lastUpdate = updateInfoRepository.getUpdateInfoByUpdateType(updateType);
        LocalDate today = LocalDate.now();
        DecimalFormat decimalFormat = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
//        lastUpdate.setUpdateDate(LocalDate.now().minusDays(1));
        if (lastUpdate == null || lastUpdate.getUpdateDate().isBefore(today)) {
            switch (updateType) {

                case ALL -> {
                    updateDeposits(today, decimalFormat);
                    updateCards(today, decimalFormat);
                    updateCredits(today, decimalFormat);
                }

                case CARD -> updateCards(today, decimalFormat);
                case DEPOSIT -> updateDeposits(today, decimalFormat);
                case CREDIT -> updateCredits(today, decimalFormat);

                default -> {
                }
            }

            if (lastUpdate == null)
                lastUpdate = new UpdateInfo(updateType, today);
            else lastUpdate.setUpdateDate(today);
            updateInfoRepository.save(lastUpdate);
        }
        if (updateType.equals(UpdateType.ALL)) {
            for (var update : updateInfoRepository.findAll()){
                update.setUpdateDate(today);
                updateInfoRepository.save(update);
            }

        }
    }

    public List<CreditStatement> getAllExpectsCreditStatements() {
        return creditStatementRepository.findCreditStatementsByCreditStatementType(CreditStatementType.EXPECTS);
    }

    public void actionStatement(Long creditStatementId, String action) {
        CreditStatement creditStatement = creditStatementRepository.findCreditStatementsByCreditStatementId(creditStatementId);
        Credit credit = creditRepository.findCreditByCredId(creditStatement.getCredit().getCredId());
        if (action.equals("APPROVE")) {
            creditStatement.setCreditStatementType(CreditStatementType.APPROVED);
            LocalDate today = LocalDate.now();
            credit.setStartDate(today);
            credit.setPeriod(creditStatement.getPeriod());
            credit.setEndDate(today.plusYears(credit.getPeriod()));
            credit.setNexUpdateDate(today.plusMonths(1));
            credit.setBalance(credit.getSum());
            credit.setPaid(true);
            credit.setActive(true);
            DebitCard card = credit.getDebitCard();
            card.setBalance(card.getBalance() + credit.getSum());

            Double monthPercent = 1 + credit.getCreditInfo().getCreditPercent() * 0.01 / 12;
            credit.setPercent(monthPercent);

            Double tempSum = 0.0;

            for (int i = 0; i < credit.getPeriod() * 12; i++) {
                tempSum += Math.pow(monthPercent, i);
            }

            Double nextPayment = Math.floor(credit.getSum() * Math.pow(monthPercent, credit.getPeriod() * 12 - 1) / tempSum);
            credit.setPayment(nextPayment);
            credit.setNextPayment(nextPayment);

            creditStatementRepository.save(creditStatement);
            creditRepository.save(credit);

        } else if (action.equals("REJECT")) {
            creditStatement.setCreditStatementType(CreditStatementType.REJECTED);
            credit.setActive(false);

            creditStatementRepository.save(creditStatement);
            creditRepository.save(credit);
        } else return;
    }

    public LocalDate getLastUpdate(UpdateType updateType) {
        UpdateInfo info = updateInfoRepository.getUpdateInfoByUpdateType(updateType);
        if (info != null)
            return info.getUpdateDate();
        else return null;
    }

    public void updateDeposits(LocalDate today, DecimalFormat decimalFormat) {
        for (var deposit : depositRepository.findAll()) {
            if (!deposit.isActive())
                continue;

            deposit.setNextUpdateDate(today);
            if (deposit.getEndDate().isEqual(today) || deposit.getEndDate().isBefore(today)) {
                deposit.setActive(false);
            } else if (deposit.getNextUpdateDate().isEqual(today) || deposit.getNextUpdateDate().isBefore(today)) {
                Double newBalance = deposit.getBalance() + deposit.getMinBalance() * (deposit.getPercent() * 0.01 / 12);
                newBalance = Double.parseDouble(decimalFormat.format(newBalance));
                deposit.setBalance(newBalance);
                deposit.setMinBalance(deposit.getBalance());
                deposit.setNextUpdateDate(today.plusMonths(1));
            }

            depositRepository.save(deposit);
        }
    }

    public void updateCards(LocalDate today, DecimalFormat decimalFormat) {
        for (var card : cardsRepository.findAll()) {

            if (!card.getActive())
                continue;

            if (card.getEndDate().isEqual(today) || card.getEndDate().isBefore(today)) {
                card.setActive(false);
            } else if (card.getNextCashbackUpdate().isEqual(today) || card.getNextCashbackUpdate().isBefore(today)) {
                Double newBalance = card.getBalance() + card.getSpendingForMonth() * (card.getCashbackPercent() * 0.01 / 12);
                newBalance = Double.parseDouble(decimalFormat.format(newBalance));
                card.setBalance(card.getBalance() - card.getCardInfo().getPrice()); // стоимость обслуживания
                card.setNextCashbackUpdate(today.plusMonths(1));
                card.setBalance(newBalance);
                card.setSpendingForMonth(0.0);
            }

            cardsRepository.save(card);
        }
    }

    public void updateCredits(LocalDate today, DecimalFormat decimalFormat) {
        for (var credit : creditRepository.findAll()) {

            if (!credit.getActive())
                continue;

            DebitCard card = credit.getDebitCard();
            Double tempSum;

            if (!credit.getPaid() && card.getBalance() >= credit.getNextPayment()) {
                card.setBalance(card.getBalance() - credit.getNextPayment());
                credit.setBalance(credit.getBalance() - credit.getNextPayment());
                credit.setPaid(true);
                credit.setNextPayment(credit.getPayment());
            }

            if (credit.getEndDate().isEqual(today) || credit.getEndDate().isBefore(today)) {
                if (card.getBalance() >= credit.getBalance()) {
                    tempSum = card.getBalance() - credit.getBalance();
                    tempSum = Double.parseDouble(decimalFormat.format(tempSum));
                    card.setBalance(tempSum);
                    credit.setBalance(0.0);
                    credit.setNextPayment(credit.getPayment());
                    credit.setPaid(true);
                    credit.setActive(false);
                } else {
                    if (credit.getNexUpdateDate().isEqual(today) || credit.getNexUpdateDate().isBefore(today)) {
                        tempSum = credit.getBalance() * credit.getPercent();
                        tempSum = Double.parseDouble(decimalFormat.format(tempSum));
                        credit.setBalance(tempSum);
                        credit.setNextPayment(credit.getNextPayment() + credit.getPayment());
                        credit.setNexUpdateDate(credit.getNexUpdateDate().plusMonths(1));
                    }
                    credit.setPaid(false);
                }
            } else if (credit.getNexUpdateDate().isEqual(today) || credit.getNexUpdateDate().isBefore(today)) {
                if (card.getBalance() >= credit.getNextPayment()) {
                    tempSum = card.getBalance() - credit.getNextPayment();
                    tempSum = Double.parseDouble(decimalFormat.format(tempSum));
                    card.setBalance(tempSum);
                    credit.setBalance(credit.getBalance() - credit.getNextPayment());
                    credit.setNextPayment(credit.getPayment());
                    credit.setPaid(true);
                } else {
                    credit.setPaid(false);
                    credit.setNextPayment(credit.getNextPayment() + credit.getPayment());
                }

                tempSum = credit.getBalance() * credit.getPercent();
                tempSum = Double.parseDouble(decimalFormat.format(tempSum));
                credit.setBalance(tempSum);
                credit.setNexUpdateDate(credit.getNexUpdateDate().plusMonths(1));

            }

            creditRepository.save(credit);
            cardsRepository.save(card);
        }
    }
}
