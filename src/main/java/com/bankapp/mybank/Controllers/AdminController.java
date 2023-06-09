package com.bankapp.mybank.Controllers;

import com.bankapp.mybank.Model.UpdateType;
import com.bankapp.mybank.Model.User;
import com.bankapp.mybank.Service.AdminService;
import com.bankapp.mybank.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    @Autowired
    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("/control-panel")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String controlPanel(@AuthenticationPrincipal User user,
                               Model model){

        model.addAttribute("admin", true);
        model.addAttribute("user", user);
        model.addAttribute("cardsUpdate", adminService.getLastUpdate(UpdateType.CARD));
        model.addAttribute("depositsUpdate", adminService.getLastUpdate(UpdateType.DEPOSIT));
        model.addAttribute("creditsUpdate", adminService.getLastUpdate(UpdateType.CREDIT));
        model.addAttribute("creditStatements", adminService.getAllExpectsCreditStatements());
        return "controlPanel";
    }

    @PostMapping("/control-panel")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String doUpdate(@RequestParam UpdateType updateType){

        adminService.doUpdate(updateType);
        return "redirect:/admin/control-panel";
    }

    @PostMapping("/control-panel/action-statement")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String actionStatement(String act,
                                  Long creditStatementId) {
        adminService.actionStatement(creditStatementId, act);
        return "redirect:/admin/control-panel";
    }

    @GetMapping("/control-panel/userInfo/{userId}")
    public String getUserInfo(@PathVariable Integer userId, Model model){
        model.addAttribute("user", userService.getUserById(userId));
        return "userInfo";
    }
}
