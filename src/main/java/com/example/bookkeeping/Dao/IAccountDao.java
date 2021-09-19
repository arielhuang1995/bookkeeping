package com.example.bookkeeping.Dao;

import com.example.bookkeeping.Controller.vo.SearchAccountVo;
import com.example.bookkeeping.Entity.Account;

import java.util.List;
import java.util.Optional;

public interface IAccountDao {
    void add(Account account);

    void delete(Integer id);

    void update(Account newAccount);

    Optional<Account> get(Integer id);

    List<Account> query(SearchAccountVo searchAccountVo);
}
