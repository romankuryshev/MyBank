package com.bankapp.mybank.Controllers;

import com.bankapp.mybank.Model.DebitCard;
import com.bankapp.mybank.Model.DebitCardInfo;
import com.bankapp.mybank.Model.Role;
import com.bankapp.mybank.Model.User;
import com.bankapp.mybank.Repository.CardsInfoRepository;
import com.bankapp.mybank.Repository.CardsRepository;
import com.bankapp.mybank.Service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cards")
public class CardsController {

    private final CardService cardService;

    @Autowired
    public CardsController(CardsInfoRepository cardsInfoRepository, CardsRepository cardsRepository, CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("")
    public String getCards(@AuthenticationPrincipal User currentUser, Model model){

        model.addAttribute("cardsInfo", cardService.getInfoAboutAllCards());
        model.addAttribute("user", currentUser);

        if(currentUser != null && currentUser.getRoles().contains(Role.ADMIN))
            return "adminCards";
        else
            return "clientCards";
    }

//    @GetMapping("/orderingCard/{card}")
//    public String orderCard(@PathVariable DebitCardInfo card,
//                            @AuthenticationPrincipal User currentUser,
//                            Model model){
//        model.addAttribute("card", card);
//        model.addAttribute("user", currentUser);
//        return "orderingCard";
//    }


    @PostMapping("/orderingCard/submit")
    @PreAuthorize("hasAuthority('USER')")
    public String submitOrderCard(@RequestParam DebitCardInfo cardInfo,
                                  @AuthenticationPrincipal User currentUser
                                  /*,@RequestParam String password)*/){
        cardService.addUserCard(currentUser, cardInfo);
        return "redirect:/cards";
    }

//    изменение карт - get запрос
    @GetMapping("{card}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateCards(@AuthenticationPrincipal User user, @PathVariable DebitCardInfo card, Model model){
//        model.addAttribute("card", cardsInfoRepository.findDebitCardInfoByCardId(card.getCardId()));
        model.addAttribute("card", card);
        model.addAttribute("user", user);
        return "cardEditing";
    }

    //    изменение карт - post запрос
    @PostMapping("/{currentCard}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String changeCard(@PathVariable DebitCardInfo currentCard,
                             @RequestParam String name,
                             @RequestParam String description,
                             @RequestParam Double price,
                             @RequestParam Double cashBackPercent,
                             @RequestParam Double operationsLimit){

        cardService.changeInfoCard(currentCard,name, description, price, cashBackPercent, operationsLimit);
        return "redirect:/cards";
    }

//    добавление новой карты
    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addNewCard(DebitCardInfo card,
                             Model model){
        cardService.addInfoCard(card);
        return "redirect:/cards";
    }
}
