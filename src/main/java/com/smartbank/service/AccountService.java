package com.smartbank.service;

import com.smartbank.model.Account;
import com.smartbank.model.Users;
import com.smartbank.repository.AccountRepository;
import com.smartbank.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService
{
    @Autowired
    UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    TransactionService transactionService;

    //Add new Account
    public String add(Long userId, String account_num,String acc_pin, String account_type, String branch,String ifsc,String aadhar)
    {
        Optional<Users> optionalUser= userRepository.findById(userId);
        if(optionalUser.isPresent())
        {
            Account acc= new Account();
            acc.setAccountNumber(account_num);
            acc.setAcc_pin(acc_pin);
            acc.setAccountType(account_type);
            acc.setUser(optionalUser.get());
            acc.setBranch(branch);
            acc.setIfsc(ifsc);
            acc.setAadhar(aadhar);

            accountRepository.save(acc);
            return "Account created successfully!";
        }
        else {
            return "user not found";
        }
    }


    //Deposits

    public String deposit(String number, String acc_pin,Double amount, String type)
    {
        Optional<Account> accOpt= Optional.ofNullable(accountRepository.findByAccountNumber(number));

        if(accOpt.isPresent())
        {
            Account acc = accOpt.get();
            if(acc_pin.equals(acc.getAcc_pin()))
            {
                acc.setBalance(acc.getBalance()+amount);
                accountRepository.save(acc);
                transactionService.recordTransaction(number, amount, type,"Success" );
                return "Deposit successful";
            }
            else
            {
                return "Invalid Pin";
            }

        }
        else
        {
            transactionService.recordTransaction(number, amount, type,"FAILED" );
            return "Account not found";
        }
    }

    //Withdraw money

    public String withdraw(String number,String acc_pin, Double amount, String type)
    {
        Optional<Account> accOpt= Optional.ofNullable(accountRepository.findByAccountNumber(number));

        if(accOpt.isPresent())
        {
            Account acc=accOpt.get();
            if(acc_pin.equals(acc.getAcc_pin()))
            {
                if(acc.getBalance()>=amount)
                {
                    acc.setBalance(acc.getBalance()-amount);
                    accountRepository.save(acc);
                    transactionService.recordTransaction(number, amount, type,"Success" );
                    return "Withdrawal successful. Remaining Balance: "+acc.getBalance();
                }
                else
                {
                    transactionService.recordTransaction(number, amount, type,"Failed" );
                    return "Insufficient Balance !";
                }
            }
            else
            {
                return "Invalid Pin";
            }

        }
        else {
            return "Account not found!";
        }
    }

    public String checkBalance(String accountNumber,String acc_pin)
    {
        Optional<Account> accOpt= Optional.ofNullable(accountRepository.findByAccountNumber(accountNumber));


        if(accOpt.isPresent())
        {
            Account acc=accOpt.get();
            if(acc_pin.equals(acc.getAcc_pin()))
            {
                return "Current Balance: "+acc.getBalance();
            }
            else {
                return "ENTER VALID PIN";
            }

        }
        else
        {
            return "Account not Found";
        }
    }

    @Transactional
    public String Transaction(String number,String acc_pin, String number2, Double amount , String type )
    {
        Optional<Account> accOpt1= Optional.ofNullable(accountRepository.findByAccountNumber(number));
        Optional<Account> accOpt2= Optional.ofNullable(accountRepository.findByAccountNumber(number2));

        if(accOpt1.isPresent() && accOpt2.isPresent())
        {
            Account acc1=accOpt1.get();
            Account acc2=accOpt2.get();

            if(acc_pin.equals(acc1.getAcc_pin()))
            {
                if(acc1.getBalance()>=amount)
                {
                    acc2.setBalance(acc2.getBalance() + amount);
                    acc1.setBalance(acc1.getBalance()-amount);
                    accountRepository.save(acc1);
                    accountRepository.save(acc2);

                    transactionService.recordTransaction(number,amount,type,"Successfully Transferred");
                    transactionService.recordTransaction(number2,amount,type,"Successfully Transferred");
                    return "Transaction Successful";
                }
                else {
                    transactionService.recordTransaction(number,amount,type," Transaction Failed");

                    return "Insufficient funds";
                }
            }
            else
            {
                return "Invalid pin";
            }
        }
        else
        {
            transactionService.recordTransaction(number,amount,type," Transaction Failed");
            return "Account not found!";
        }
    }

}
