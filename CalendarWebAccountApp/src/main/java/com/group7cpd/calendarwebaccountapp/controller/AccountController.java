package com.group7cpd.calendarwebaccountapp.controller;

import com.group7cpd.calendarwebaccountapp.model.Account;
import com.group7cpd.calendarwebaccountapp.repository.AccountRepository;
import com.group7cpd.calendarwebaccountapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountController {
    @Autowired
    AccountService accountService;

    @GetMapping("/accounts")
    private List<Account> getAllAccounts(){
        return accountService.getAllAccounts();
    }

    @GetMapping("/account/{id}")
    private Account getAccount(@PathVariable("id") int id){
        return accountService.getAccountById(id);
    }

    @GetMapping("/account/user/{name}")
    public Account getAccountByName(@PathVariable("name") String name){
        return accountService.getAccountByName(name);
    }

    @GetMapping("/account/login/{name}/{pass}")
    public Account logInAccount(@PathVariable("name") String name, @PathVariable("pass") String pass){
        return accountService.logInAccount(name, pass);
    }

    @PostMapping("/account/deletion/{name}/{pass}")
    public void deleteAccount(@PathVariable("name") String name, @PathVariable("pass") String pass){
        accountService.deleteAccount(name, pass);
    }

    @PostMapping("/account/register")
    public void registerAccount(@RequestBody Account newAcc) {
        accountService.registerAccount(newAcc);
    }

    @PostMapping("/account/{name}/events")
    public void addEvents(@PathVariable("name") String name, @RequestBody String events) {
        accountService.addEvent(name, events);
    }
}
