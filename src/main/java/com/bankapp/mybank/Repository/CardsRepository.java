package com.bankapp.mybank.Repository;

import com.bankapp.mybank.Model.DebitCard;
import com.bankapp.mybank.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardsRepository extends JpaRepository<DebitCard, Integer> {
    List<DebitCard> findAllByUser(User user);
    DebitCard findByCardNumber(String cardNumber);
}
