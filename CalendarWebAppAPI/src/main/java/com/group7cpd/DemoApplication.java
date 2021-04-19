package com.group7cpd;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group7cpd.model.Account;
import com.group7cpd.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(AccountService accountService){
        return args -> {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<Account>> typeReference = new TypeReference<List<Account>>(){};
            InputStream inputStream = TypeReference.class.getResourceAsStream("/json/accounts.json");
            try{
                List<Account> accounts = mapper.readValue(inputStream,typeReference);
                accountService.save(accounts);
                System.out.println("Accounts saved.");
            }catch(IOException e){
                System.out.println("Accounts failed to save. Please check if object has default constructor.");
            }
        };

    }

}
