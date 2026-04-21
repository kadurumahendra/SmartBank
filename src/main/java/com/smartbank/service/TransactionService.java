package com.smartbank.service;

import com.smartbank.model.Account;
import com.smartbank.model.Transaction;
import com.smartbank.repository.AccountRepository;
import com.smartbank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService
{
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;


    public void recordTransaction(String number, Double amount, String type, String status) {
        Account account = accountRepository.findByAccountNumber(number);

        if (account != null) {
            Transaction transaction = new Transaction(account, amount, type, status);
            transactionRepository.save(transaction);
        }


    }

    public List<Transaction> getTransactionsByAccountId(Long accountId)
    {
        return transactionRepository.findByAccount_Id(accountId);

    }


}
