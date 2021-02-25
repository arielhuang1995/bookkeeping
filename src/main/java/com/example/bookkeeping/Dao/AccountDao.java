package com.example.bookkeeping.Dao;

import com.example.bookkeeping.Controller.vo.SearchAccountVo;
import com.example.bookkeeping.Entity.Account;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AccountDao {
    //TODO CRUD...

    private List<Account> accountList = new ArrayList<>();

    public void add(Account account){
        account.setId(findMaxId() + 1);
        accountList.add(account);
    }

    public void delete(Integer id){
        accountList.forEach(
                account -> {
                    if (account.getId().equals(id)) {
                        accountList.remove(account);
                    }
                });
    }

    public void update(Account newAccount){
        accountList.forEach(
                account -> {
                    if (account.getId().equals(newAccount.getId())) {
                        Integer index = accountList.indexOf(account);
                        newAccount.setUpdateTime(new Date());
                        accountList.set(index, newAccount);
                    }
                });
    }

    public Account get(Integer id){
        return this.accountList.stream()
                .filter(account -> id.equals(account.getId()))
                .findAny().orElse(null);
    }

    public List<Account> search(SearchAccountVo searchAccountVo){
        return accountList.stream()
                        .filter(
                                account ->
                                        account.getCreateTime().after(searchAccountVo.getStartDate())
                                                && account.getCreateTime().before(searchAccountVo.getEndDate()))
                        .collect(Collectors.toList());
    }

    private Integer findMaxId() {
        return this.accountList.stream().mapToInt(Account::getId).max().orElse(0);
    }

}
