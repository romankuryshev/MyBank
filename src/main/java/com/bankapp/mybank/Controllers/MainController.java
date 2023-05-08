package com.bankapp.mybank.Controllers;

import com.bankapp.mybank.Model.Role;
import com.bankapp.mybank.Model.User;
import com.bankapp.mybank.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;
import java.util.Set;

@Controller
public class MainController {

    private final UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String getHome(@AuthenticationPrincipal User currentUser,
                          Model model){


        if(currentUser != null){
            model.addAttribute("user", currentUser);
            if(currentUser.getRoles().contains(Role.ADMIN)) model.addAttribute("admin", true);
        } else model.addAttribute("user", null);
        return "home";
    }

    @GetMapping("/test")
    public String getTest(Model model){
        return "test";
    }

    @GetMapping("/registration")
    public String getRegistration(Model model){
        return "registration";
    }

    @PostMapping("/registration")
    public String getRegistration(User user, Map<String, Object> model){
        if(userService.addUser(user)){
            return "redirect:/login";
        }
        else {
            model.put("message", "Users exists");
            return "registration";
        }
    }
}
