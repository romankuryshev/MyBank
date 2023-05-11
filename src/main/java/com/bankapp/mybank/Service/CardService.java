package com.bankapp.mybank.Service;

import com.bankapp.mybank.Model.DebitCard;
import com.bankapp.mybank.Model.DebitCardInfo;
import com.bankapp.mybank.Model.User;
import com.bankapp.mybank.Repository.CardsInfoRepository;
import com.bankapp.mybank.Repository.CardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CardService {
    private static String BIN = "200000";
    private final CardsInfoRepository cardsInfoRepository;
    private final CardsRepository cardsRepository;

    @Autowired
    public CardService(CardsInfoRepository cardsInfoRepository, CardsRepository cardsRepository) {
        this.cardsInfoRepository = cardsInfoRepository;
        this.cardsRepository = cardsRepository;
    }

    public List<DebitCardInfo> getInfoAboutAllCards() {
        return cardsInfoRepository.findDebitCardInfoByActive(true);
    }

    public void addUserCard(User user, DebitCardInfo card /*, String password*/) {

        String num = randomGenerator(card);
        String cvc = cvcGenerator();

        while (cardsRepository.findByCardNumber(num) != null) {
            num = randomGenerator(card);
        }

        DebitCard newCard = new DebitCard(user, card, /*password,*/ num, cvc);
        newCard.setNextCashbackUpdate(LocalDate.now().plusMonths(1));
        newCard.setCashbackPercent(card.getCashBackPercent());
        cardsRepository.save(newCard);
    }

    public void changeInfoCard(DebitCardInfo currentCard,
                               String name,
                               String description,
                               Double price,
                               Double cashBackPercent,
                               Double operationsLimit) {

        currentCard.setName(name);
        currentCard.setDescription(description);
        currentCard.setPrice(price);
        currentCard.setCashBackPercent(cashBackPercent);
        currentCard.setOperationsLimit(operationsLimit);

        cardsInfoRepository.save(currentCard);
    }

    public void addInfoCard(DebitCardInfo card) {
        card.setActive(true);
        cardsInfoRepository.save(card);
    }

    public void hideInfoCard(Integer cardId){
        DebitCardInfo card = cardsInfoRepository.findDebitCardInfoByCardId(cardId);
        card.setActive(false);
        cardsInfoRepository.save(card);
    }

    private String randomGenerator(DebitCardInfo debitCardInfo) {
        StringBuilder number = new StringBuilder(BIN);
        number.append(debitCardInfo.getBankProgramCode());

        for (int i = 0; i < 7; i++) {
            number.append((int) (Math.random() * 10));
        }

        int sum = 0;
        for(int i = 0; i < number.length(); i++){
            if(i % 2 == 0){
                int temp = (((int) number.charAt(i)) - '0') * 2;
                if( temp > 9) temp -= 9;
                sum += temp;
            } else sum += number.charAt(i) - '0';
        }

        if (sum % 10 == 0)
            number.append(0);
        else number.append(10 - sum % 10);
        return number.toString();
    }

    private String cvcGenerator() {
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            number.append((int) (Math.random() * 10));
        }
        return number.toString();
    }

    public static boolean isLuhn(String number) {

        int sum = 0;

        for(int i = 0; i < number.length(); i++){
            if(i % 2 == 0){
                int temp = (((int) number.charAt(i)) - '0') * 2;
                if( temp > 9) temp -= 9;
                sum += temp;
            } else sum += number.charAt(i) - '0';
        }

        if (sum % 10 == 0) return true;
        else return false;
    }

}


