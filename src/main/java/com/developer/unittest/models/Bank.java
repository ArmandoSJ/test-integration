package com.developer.unittest.models;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class Bank {
    private List<Account> accounts = new ArrayList<>();
    private String name;

    public void addAccount(Account account){
        accounts.add(account);
        account.setBank(this);
    }

    public void transfer(Account accountOrigen, Account accountDestination, BigDecimal amount){
        accountOrigen.debit(amount);
        accountDestination.credit(amount);
    }
}
