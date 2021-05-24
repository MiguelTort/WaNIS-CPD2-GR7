package com.group7cpd.calendarwebaccountapp.service;

import com.group7cpd.calendarwebaccountapp.model.Account;
import com.group7cpd.calendarwebaccountapp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<Account>();
        accountRepository.findAll().forEach(student -> accounts.add(student));
        System.out.println(accounts);
        return accounts;
    }

    public Account getAccountById(int id) {
        return accountRepository.findById(id).get();
    }

    public Account getAccountByName(String name) {
        List<Account> accounts = new ArrayList<Account>();
        accountRepository.findAll().forEach(student -> accounts.add(student));
        Account account = null;
        int index = 0;
        while(index < accounts.size()){
            if(accounts.get(index).getName().compareTo(name)==0){
                account = accounts.get(index);
                break;
            }
            index++;
        }
        return account;
    }

    public Account logInAccount(String name, String pass) {
        List<Account> accounts = new ArrayList<Account>();
        accountRepository.findAll().forEach(student -> accounts.add(student));
        Account account = null;
        int index = 0;
        while(index < accounts.size()){
            if(accounts.get(index).getName().compareTo(name)==0){
                account = accounts.get(index);
                break;
            }
            index++;
        }
        if(account == null){
            return null;
        }
        if(account.getPassword().compareTo(pass)==0){
            return account;
        }
        return null;
    }

    public void registerAccount(Account newAcc) {
        newAcc.setId((int)accountRepository.count()+1);
        accountRepository.save(newAcc);
    }

    public void deleteAccount(String name, String pass) {
        List<Account> accounts = new ArrayList<Account>();
        accountRepository.findAll().forEach(student -> accounts.add(student));
        Account account = new Account();
        account.setName("[DELETED]");
        account.setPassword("[DELETED]");
        account.setEvents("[DELETED]");
        int index = 0;
        while(index < accounts.size()){
            if(accounts.get(index).getName().compareTo(name)==0){
                if(accounts.get(index).getPassword().compareTo(pass)==0){
                    account.setId(index+1);
                    accountRepository.save(account);
                    break;
                }
            }
            index++;
        }
    }

    public void addEvent(String name, String events) {
        List<Account> accounts = new ArrayList<Account>();
        accountRepository.findAll().forEach(student -> accounts.add(student));
        Account account = null;
        int index = 0;
        while(index < accounts.size()){
            if(accounts.get(index).getName().compareTo(name)==0){
                account = accounts.get(index);
                break;
            }
            index++;
        }
        account.setEvents(events);
        accountRepository.save(account);
    }
}
