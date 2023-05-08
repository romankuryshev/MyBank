package com.bankapp.mybank.Controllers;

import com.bankapp.mybank.Model.DepositInfo;
import com.bankapp.mybank.Model.Role;
import com.bankapp.mybank.Model.User;
import com.bankapp.mybank.Service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/deposits")
public class DepositsController {

    private final DepositService depositService;

    @Autowired
    public DepositsController(DepositService depositService) {
        this.depositService = depositService;
    }

    @GetMapping("")
    public String getDeposits(@AuthenticationPrincipal User currentUser, Model model) {

        if (currentUser != null && currentUser.getRoles().contains(Role.ADMIN)) model.addAttribute("admin", true);
        else model.addAttribute("admin", false);

        model.addAttribute("user", currentUser);
        model.addAttribute("depositsInfo", depositService.getAllDeposits());
        return "deposits";
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addDeposit(DepositInfo depositInfo){
        depositService.addNewDepositInfo(depositInfo);
        return "redirect:/deposits";
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String hideDeposit(Long depositId){
        depositService.hideDeposit(depositId);
        return "redirect:/deposits";
    }

    @GetMapping("/{depositInfo}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editDeposit(@PathVariable DepositInfo depositInfo, Model model){
        model.addAttribute("deposit", depositInfo);
        return "depositEditing";
    }

    @PostMapping("/{depositId}")
    public String editDepositPost(@AuthenticationPrincipal User user,
                                  @PathVariable Long depositId,
                                  DepositInfo depositInfo,
                                  Model model){
        depositService.updateDeposit(depositId, depositInfo);
        model.addAttribute("user", user);
        return "redirect:/deposits";
    }

    @PostMapping("/open")
    @PreAuthorize("hasAuthority('USER')")
    public String openDeposit(@RequestParam DepositInfo depositInfo,
                            @RequestParam Integer period,
                            @AuthenticationPrincipal User user){
        depositService.addUserDeposit(user, depositInfo, period);
        return "redirect:/deposits";
    }
}
