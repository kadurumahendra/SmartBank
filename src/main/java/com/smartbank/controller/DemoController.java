package com.smartbank.controller;

import com.smartbank.model.Transaction;
import com.smartbank.service.AccountService;
import com.smartbank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/api/account")
public class DemoController
{
    @Autowired
    AccountService accsrc;
    @Autowired
    TransactionService transactionService;

    //Deposit
    @PostMapping("/deposit")
    public String deposit(@RequestParam String number,@RequestParam String acc_pin, @RequestParam Double amount, @RequestParam String type)
    {
               String result = accsrc.deposit(number, acc_pin, amount, type);
               if(result.equalsIgnoreCase("Deposit successful"))
               {
                   return "creditSuccess";
               }
               else
               {
                   return "deposit";
               }

    }

    //Transaction
    @PostMapping("/transaction")
    public String transaction(@RequestParam String number,@RequestParam String acc_pin, @RequestParam String number2, @RequestParam Double amount, @RequestParam String type)
    {
        String result = accsrc.Transaction(number,acc_pin,number2,amount,type);
        if(result.equalsIgnoreCase("Invalid pin"))
        {
            return "transaction";
        }
        else {
            return "transactionSuccess";
        }
    }

    //Withdraw
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String number,@RequestParam String acc_pin, @RequestParam Double amount, @RequestParam  String type)
    {
        String result= accsrc.withdraw(number,acc_pin,amount, type);
        if(result.equalsIgnoreCase("invalid pin") || result.equalsIgnoreCase("Account not found!"))
        {
            return "withdraw";
        }
        else {
            return "withdrawSuccess";
        }
    }

    //CheckBalance
    @PostMapping("/balance")
    public String balance(@RequestParam("number") String number, @RequestParam("acc_pin") String acc_pin, Model model
    ) {
        String result = accsrc.checkBalance(number, acc_pin);
        model.addAttribute("result", result);
        return "balanceResult";  // this is balanceResult.html
    }



    @PostMapping("/history")
    public List<Transaction> getTransactionHistory(@RequestParam Long id)
    {
        return transactionService.getTransactionsByAccountId(id);
    }

}
