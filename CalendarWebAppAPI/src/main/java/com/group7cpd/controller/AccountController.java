package com.group7cpd.controller;

import com.group7cpd.service.AccountService;
import com.group7cpd.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/account/{user}")
    public Account getAccountByUser(@PathVariable("user") String userName) {
        return accountService.getAccountByUser(userName);
    }

    @GetMapping("/account/{user}/string")
    public String getAccountByUserString(@PathVariable("user") String userName) {
        return accountService.getAccountByUser(userName).toString();
    }

    @GetMapping("/greet")
    public String greeter(){
        accountService.record();
        return "Hello, world!";
    }

    @PostMapping("account/register")
    public Account addAccount(@RequestBody Account newAcc){
        return accountService.addAccount(newAcc);
    }

    @PostMapping("account/edit")
    public Account editAccount(@RequestBody Account newAcc){
        return accountService.editAccount(newAcc);
    }
}