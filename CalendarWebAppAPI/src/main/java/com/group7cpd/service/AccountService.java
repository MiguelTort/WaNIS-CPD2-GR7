package com.group7cpd.service;

import com.group7cpd.model.Account;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    List<Account> accounts = new ArrayList<>();

    public List<Account> getAllAccounts(){
        return accounts;
    }

    public Account getAccountByUser(String u){
        int index = -1;
        for(int x = 0; x < accounts.size(); x++){
            Account accSearch = accounts.get(x);
            if(accSearch.getUser().compareTo(u)==0){
                index = x;
                break;
            }
        }
        if(index == -1){
            return null;
        }
        return accounts.get(index);
    }

    public Account addAccount(Account newAcc) {
        accounts.add(newAcc);
        record();
        return getAccountByUser(newAcc.getUser());
    }

    public void save(List<Account> newAccounts) {
        for(int x = 0; x < newAccounts.size(); x++){
            accounts.add(newAccounts.get(x));
        }
    }

    public void record(){
        try{
            FileWriter myWriter = new FileWriter("src/main/resources/json/accounts.json");
            myWriter.write(accounts.toString());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        }catch(IOException e){
            System.out.println("Did not successfully write to the file.");
        }
    }

    public Account editAccount(Account newAcc) {
        int index = -1;
        for(int x = 0; x < accounts.size(); x++){
            Account accSearch = accounts.get(x);
            if(accSearch.getUser().compareTo(newAcc.getUser())==0){
                index = x;
                break;
            }
        }
        if(index == -1){
            System.out.println("Account editing failed");
            return null;
        }
        accounts.set(index, newAcc);
        record();
        return accounts.get(index);
    }
}
