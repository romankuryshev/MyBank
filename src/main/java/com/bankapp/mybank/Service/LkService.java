package com.bankapp.mybank.Service;

import com.bankapp.mybank.Model.Credit;
import com.bankapp.mybank.Model.DebitCard;
import com.bankapp.mybank.Model.Deposit;
import com.bankapp.mybank.Model.User;
import com.bankapp.mybank.Repository.CardsRepository;
import com.bankapp.mybank.Repository.CreditRepository;
import com.bankapp.mybank.Repository.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class LkService {

    private final CardsRepository cardsRepository;
    private final DepositRepository depositRepository;
    private final CreditRepository creditRepository;

    @Autowired
    public LkService(CardsRepository cardsRepository, DepositRepository depositRepository, CreditRepository creditRepository) {
        this.cardsRepository = cardsRepository;
        this.depositRepository = depositRepository;
        this.creditRepository = creditRepository;
    }

    public List<DebitCard> getUserCards(User user) {
        List<DebitCard> cards = new ArrayList<>(cardsRepository.findAllByUser(user));
        return cards.stream().filter(DebitCard::getActive).sorted(Comparator.comparing(DebitCard::getCardNumber)).collect(Collectors.toList());
    }

    public List<Deposit> getUserDeposits(User user) {
        List<Deposit> deposits = new ArrayList<>(depositRepository.findAllByUser(user));
        return deposits.stream().filter(Deposit::isActive).sorted(Comparator.comparing(Deposit::getDepId)).collect(Collectors.toList());
    }

    public List<Credit> getUserCredits(User user){
        List<Credit> credits = new ArrayList<>(creditRepository.findAllCreditsByUser(user));
        System.out.println(credits);
        return credits.stream().filter(Credit::getActive).sorted(Comparator.comparing(Credit::getCredId)).collect(Collectors.toList());
    }

    public String deleteUserCard(User user, String card){

        DebitCard debitCard = cardsRepository.findByCardNumber(card);
        if(debitCard == null)
            return "Ошибка.";

        if(creditRepository.findCreditByDebitCard(debitCard) != null)
            return "Ошибка.";

        if(debitCard.getBalance() != 0.0)
            return "Сначала переведите средства на другую карту.";

        if(debitCard.getUser().getUserId().equals(user.getUserId())){
            debitCard.setActive(false);
            cardsRepository.save(debitCard);
            return null;
        } else return "Ошибка";
    }

    public String deleteUserDeposit(User user, Long depId) {
        Deposit deposit = depositRepository.findByDepId(depId);
        if(deposit != null && deposit.getUser().getUserId().equals(user.getUserId()) && deposit.getBalance() == 0.0){
            deposit.setActive(false);
            depositRepository.save(deposit);
            return "Выполнено успешно!";
        } else return "Ошибка";
    }

    public String doTransfer(User currentUser, String currentCard,
                              Double amount, String cardNumberRecipient) {
        DebitCard inputCard = cardsRepository.findByCardNumber(currentCard);
        DecimalFormat decimalFormat = new DecimalFormat( "#.##", new DecimalFormatSymbols(Locale.US));
        if (inputCard != null && inputCard.getUser().getUserId().equals(currentUser.getUserId())) {

            DebitCard recipientCard = cardsRepository.findByCardNumber(cardNumberRecipient);
            if (recipientCard == null || !recipientCard.getActive()) {
                return "Неверный номер карты.";
            }

            if (inputCard.getBalance() < amount) {
                return "Недостаточно средств.";
            }

            if (amount <= 0) {
                return  "Неверная сумма перевода.";
            }

            inputCard.setBalance(Double.parseDouble(decimalFormat.format(inputCard.getBalance() - amount)));
            inputCard.setSpendingForMonth(Double.parseDouble(decimalFormat.format(inputCard.getSpendingForMonth() + amount)));
            recipientCard.setBalance(Double.parseDouble(decimalFormat.format(recipientCard.getBalance() + amount)));

            cardsRepository.save(inputCard);
            cardsRepository.save(recipientCard);
        }
        return  "Выполнено успешно!";
    }

    public String topUpDeposit(User user, String cardNumber, Long depositId, Double sum) {
        if (user == null || cardNumber == null || depositId == null || sum == null || sum <= 0)
            return "Ошибка.";
        DecimalFormat decimalFormat = new DecimalFormat( "#.##", new DecimalFormatSymbols(Locale.US));
        DebitCard card = cardsRepository.findByCardNumber(cardNumber);
        Deposit deposit = depositRepository.findByDepId(depositId);

        if( sum > card.getBalance())
            return "Недостаточно средств.";

        if (deposit.getUser().getUserId().equals(user.getUserId()) &&
                card.getUser().getUserId().equals(user.getUserId())
                && card.getActive() && deposit.isActive()) {

            if (deposit.isFirstOpen()) {
                sum = Double.parseDouble(decimalFormat.format(sum));
                deposit.setStartBalance(sum);
                deposit.setBalance(sum);
                deposit.setMinBalance(sum);
                deposit.setStartDate(LocalDate.now());
                deposit.setNextUpdateDate(deposit.getStartDate().plusMonths(1));
                deposit.setEndDate(deposit.getStartDate().plusMonths(deposit.getPeriod()));
                deposit.setFirstOpen(false);
            } else {
                deposit.setBalance(Double.parseDouble(decimalFormat.format(deposit.getBalance() + sum)));
            }

            card.setBalance(Double.parseDouble(decimalFormat.format(card.getBalance() - sum)));

            cardsRepository.save(card);
            depositRepository.save(deposit);

            return "Выполнено успешно!";
        } else return "Ошибка.";
    }

    public String depositTransfer(User user, String cardNumber, Long depositId, Double sum){
        if (user == null || cardNumber == null || depositId == null || sum == null || sum <= 0)
            return "Ошибка";

        DebitCard card = cardsRepository.findByCardNumber(cardNumber);
        Deposit deposit = depositRepository.findByDepId(depositId);
        DecimalFormat decimalFormat = new DecimalFormat( "#.##", new DecimalFormatSymbols(Locale.US));

        if(sum > deposit.getBalance()){
            return "Недостаточно средств.";
        }

        if (deposit.getUser().getUserId().equals(user.getUserId()) &&
                card.getUser().getUserId().equals(user.getUserId()) && card.getActive() && deposit.isActive()) {

            deposit.setBalance(Double.parseDouble(decimalFormat.format(deposit.getBalance() - sum)));
            card.setBalance(Double.parseDouble(decimalFormat.format(card.getBalance() + sum)));

            if(deposit.getBalance() < deposit.getMinBalance()) {
                deposit.setMinBalance(deposit.getBalance());
            }

            cardsRepository.save(card);
            depositRepository.save(deposit);
        }

        return "Выполнено успешно!";
    }

}
