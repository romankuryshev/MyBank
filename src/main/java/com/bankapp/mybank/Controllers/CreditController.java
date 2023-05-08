package com.bankapp.mybank.Controllers;

import com.bankapp.mybank.Model.Role;
import com.bankapp.mybank.Model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/credits")
public class CreditController {

    @GetMapping("")
    public String getCredits(@AuthenticationPrincipal User currentUser, Model model){

        if (currentUser != null && currentUser.getRoles().contains(Role.ADMIN)) model.addAttribute("admin", true);
        else model.addAttribute("admin", false);

        model.addAttribute("user", currentUser);

        return "credits";
    }
}
