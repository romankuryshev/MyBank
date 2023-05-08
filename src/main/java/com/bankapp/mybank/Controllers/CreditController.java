package com.bankapp.mybank.Controllers;

import com.bankapp.mybank.Model.CreditInfo;
import com.bankapp.mybank.Model.DepositInfo;
import com.bankapp.mybank.Model.Role;
import com.bankapp.mybank.Model.User;
import com.bankapp.mybank.Service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/credits")
public class CreditController {

    private final CreditService creditService;

    @Autowired
    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @GetMapping("")
    public String getCredits(@AuthenticationPrincipal User currentUser, Model model){

        if (currentUser != null && currentUser.getRoles().contains(Role.ADMIN)) model.addAttribute("admin", true);
        else model.addAttribute("admin", false);

        model.addAttribute("user", currentUser);
        model.addAttribute("creditsInfo", creditService.getAllCredits());

        return "credits";
    }

    @PostMapping("/addCredit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addCreditInfo(CreditInfo creditInfo) {
        creditService.addCreditInfo(creditInfo);
        return "redirect:/credits";
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String hideCreditInfo(Long creditId){
        creditService.hideCredit(creditId);
        return "redirect:/credits";
    }

    @GetMapping("/{creditId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editCredit(@PathVariable Long creditId,
                             Model model){
        model.addAttribute("creditInfo", creditService.getCreditInfo(creditId));
        return "creditEditing";
    }

    @PostMapping("/{creditId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editCreditPost(@PathVariable Long creditId,
                                 CreditInfo creditInfo){
        creditService.updateCredit(creditId, creditInfo);
        return "redirect:/credits";
    }


}
