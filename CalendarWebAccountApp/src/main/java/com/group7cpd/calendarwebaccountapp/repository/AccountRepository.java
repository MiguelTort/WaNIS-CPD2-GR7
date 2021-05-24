package com.group7cpd.calendarwebaccountapp.repository;
import com.group7cpd.calendarwebaccountapp.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Integer> {
}