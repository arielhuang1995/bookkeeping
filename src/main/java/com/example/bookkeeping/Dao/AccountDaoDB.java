package com.example.bookkeeping.Dao;

import com.example.bookkeeping.Controller.vo.SearchAccountVo;
import com.example.bookkeeping.Entity.Account;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDaoDB implements IAccountDao {

    //call jpa
    private final List<Account> db = new ArrayList<>();

    @Override
    public void add(Account account) {
        throw new NotImplementedException();

    }

    @Override
    public void delete(Integer id) {
        throw new NotImplementedException();
    }

    @Override
    public void update(Account newAccount) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<Account> get(Integer id) {
//        return Optional.empty();
        throw new NotImplementedException();
    }

    @Override
    public List<Account> query(SearchAccountVo searchAccountVo) {
        throw new NotImplementedException();
    }
}
