package com.example.bookkeeping.Dao;

import com.example.bookkeeping.Controller.vo.SearchAccountVo;
import com.example.bookkeeping.Entity.Account;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
//@RequiredArgsConstructor
public class AccountDaoDB implements IAccountDao {

    //call jpa
    @Autowired
    private AccountRepository accountRepository;
//    private final AccountRepository accountRepository;

//    public AccountDaoDB(AccountRepository accountRepository) {
//        this.accountRepository = accountRepository;
//    }

    @Override
    @Transactional
    public void add(Account account) {
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        accountRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(Account newAccount) {
        accountRepository.save(newAccount);
    }

    @Override
    public Optional<Account> get(Integer id) {
        return accountRepository.findById(id);
    }

    @Override
    public List<Account> query(SearchAccountVo searchAccountVo) {
        return accountRepository.findByCreateTimeBetween(searchAccountVo.getStartDate(), searchAccountVo.getEndDate());
    }
}
