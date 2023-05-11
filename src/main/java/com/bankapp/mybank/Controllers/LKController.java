package com.bankapp.mybank.Controllers;


import com.bankapp.mybank.Model.Role;
import com.bankapp.mybank.Model.User;
import com.bankapp.mybank.Service.LkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/profile")
public class LKController {
    private final LkService lkService;

    @Autowired
    public LKController(LkService lkService) {
        this.lkService = lkService;
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('USER')")
    public String getProfile(@AuthenticationPrincipal User currentUser,
                             Model model){
        if(currentUser.getRoles().contains(Role.ADMIN)){
            model.addAttribute("admin", true);
        }
        model.addAttribute("userCards", lkService.getUserCards(currentUser));
        model.addAttribute("userDeposits", lkService.getUserDeposits(currentUser));
        model.addAttribute("userCredits", lkService.getUserCredits(currentUser));
        return "profile";
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('USER')")
    public String transferMoney(@AuthenticationPrincipal User currentUser,
                                @RequestParam String currentCard,
                                @RequestParam Double amount,
                                @RequestParam String cardNumberRecipient,
                                Model model){

        String message = lkService.doTransfer(currentUser,currentCard, amount, cardNumberRecipient);
        model.addAttribute("message", message);
        model.addAttribute("cardMessage", "transfer" + currentCard);
        return getProfile(currentUser, model);
    }

    @PostMapping("/top-up")
    @PreAuthorize("hasAuthority('USER')")
    public String topUpDeposit(@AuthenticationPrincipal User user,
                               @RequestParam Long depositId,
                               @RequestParam Double depositSum,
                               @RequestParam String cardNumber,
                               Model model){

        model.addAttribute("message", lkService.topUpDeposit(user, cardNumber, depositId, depositSum));
        model.addAttribute("cardMessage", "topUpDeposit" + depositId);
        return getProfile(user, model);
    }

    @PostMapping("/delete-card")
    @PreAuthorize("hasAuthority('USER')")
    public String deleteCard(@AuthenticationPrincipal User user,
                             @RequestParam String cardNumber,
                             Model model) {

        model.addAttribute("message", lkService.deleteUserCard(user, cardNumber));
        model.addAttribute("cardMessage", "delete" + cardNumber);
        return getProfile(user, model);
    }

    @PostMapping("/delete-deposit")
    @PreAuthorize("hasAuthority('USER')")
    public String deleteDeposit(@AuthenticationPrincipal User user,
                                @RequestParam Long depId,
                                Model model){

        String message = lkService.deleteUserDeposit(user, depId);
        model.addAttribute("message", message);
        model.addAttribute("cardMessage", "deleteDeposit" + depId);
        return getProfile(user, model);
    }

    @PostMapping("/depositTransfer")
    @PreAuthorize("hasAuthority('USER')")
    public String transferDeposit(@AuthenticationPrincipal User user,
                                  @RequestParam Long depositId,
                                  @RequestParam Double depositSum,
                                  @RequestParam String cardNumber,
                                  Model model){

        String message = lkService.depositTransfer(user, cardNumber, depositId, depositSum);
        model.addAttribute("message", message);
        model.addAttribute("cardMessage", "chooseDepositTransf" + depositId.toString());
        return getProfile(user, model);
    }
}
