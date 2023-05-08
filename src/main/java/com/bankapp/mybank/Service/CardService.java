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
    private final CardsInfoRepository cardsInfoRepository;
    private final CardsRepository cardsRepository;

    @Autowired
    public CardService(CardsInfoRepository cardsInfoRepository, CardsRepository cardsRepository) {
        this.cardsInfoRepository = cardsInfoRepository;
        this.cardsRepository = cardsRepository;
    }

    public List<DebitCardInfo> getInfoAboutAllCards() {
        return cardsInfoRepository.findAll();
    }

    public void addUserCard(User user, DebitCardInfo card /*, String password*/) {

        String num = randomGenerator();
        String cvc = cvcGenerator();

        while (cardsRepository.findByCardNumber(num) != null) {
            num = randomGenerator();
        }

        DebitCard newCard = new DebitCard(user, card, /*password,*/ num, cvc);
        newCard.setNextCashbackUpdate(LocalDate.now().plusMonths(1));
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
        cardsInfoRepository.save(card);
    }

    private String randomGenerator() {
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            number.append((int) (Math.random() * 10));
        }
        return number.toString();
    }

    private String cvcGenerator() {
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            number.append((int) (Math.random() * 10));
        }
        return number.toString();
    }
}


