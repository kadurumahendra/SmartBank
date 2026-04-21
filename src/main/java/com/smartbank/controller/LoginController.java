package com.smartbank.controller;


import com.smartbank.model.Users;
import com.smartbank.repository.UserRepository;
import com.smartbank.service.AccountService;
import com.smartbank.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/log")
public class LoginController
{
    @Autowired
    UserService userService;

    @Autowired
    AccountService accountService;

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password ,  Model model,
                        HttpSession session)
    {
        String result=userService.login(username, password);
        if(result.contains("Login successful! Welcome "))
        {
            Users user = userService.getUserByUsername(username);
            session.setAttribute("loggedUser", user);


            // Store user in session
            session.setAttribute("loggedUser", user);
            model.addAttribute("username",username);
            return "dashboard";
        }
        else
        {
            model.addAttribute("error",result);
            return "login";
        }

    }

    @PostMapping("/signin")
    public String signin(@RequestParam String username, @RequestParam String password, @RequestParam String email, Model model, HttpSession session)
    {
        String result= userService.Signin(username,password,email);
        System.out.println("Signup result: " + result);
        if(result.toLowerCase().contains("account created"))
        {
            Users user = userService.getUserByUsername(username);
            session.setAttribute("loggedUser", user);

            return "addAccount";
        }
        else {
            model.addAttribute("message",result);
            return "signup";
        }

    }

    @PostMapping("/add")
    public String addAcc(@RequestParam String account_num,
                         @RequestParam String acc_pin,
                         @RequestParam String account_type,
                         @RequestParam String branch,
                         @RequestParam String irfc,
                         @RequestParam String aadhar,
                         @RequestParam Long userId,Model model)
    {
        String result= accountService.add(userId,account_num,acc_pin,account_type,branch,irfc,aadhar);
        if(result.toLowerCase().contains("account created successfully!"))
        {
            model.addAttribute("message",result);
            return "dashboard";
        }
        else {
            model.addAttribute("message",result);
            return "addAccount";
        }
    }

}
