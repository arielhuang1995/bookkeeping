package com.example.bookkeeping.Dao;

import com.example.bookkeeping.Controller.vo.SearchAccountVo;
import com.example.bookkeeping.Entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnProperty(value = "app.api.factory", havingValue = "db")
public class AccountDaoDB implements IAccountDao {

    //call jpa
    @Autowired
    private AccountRepository accountRepository;

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
