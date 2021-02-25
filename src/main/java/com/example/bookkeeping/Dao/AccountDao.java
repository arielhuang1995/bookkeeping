package com.example.bookkeeping.Dao;

import com.example.bookkeeping.Controller.vo.SearchAccountVo;
import com.example.bookkeeping.Entity.Account;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AccountDao {
    //TODO CRUD...

    private final List<Account> accountList = new ArrayList<>();

    public void add(Account account){
        account.setId(findMaxId() + 1);
        accountList.add(account);
    }

    public void delete(Integer id){
//        accountList.forEach(
//                account -> {
//                    if (account.getId().equals(id)) {
//                        accountList.remove(account);
//                    }
//                });
        Optional<Account> accountOpt = this.get(id);
        accountOpt.ifPresent(this.accountList::remove);
    }

    public void update(Account newAccount){
//        accountList.forEach(
//                account -> {
//                    if (account.getId().equals(newAccount.getId())) {
//                        Integer index = accountList.indexOf(account);
//                        newAccount.setUpdateTime(LocalDateTime.now());
//                        accountList.set(index, newAccount);
//                    }
//                });


        Optional<Account> account = this.get(newAccount.getId());
        account.ifPresent(a -> {
            int accountIndex = accountList.indexOf(a);
            newAccount.setUpdateTime(LocalDateTime.now());

            accountList.set(accountIndex, newAccount);
        });
    }

    public Optional<Account> get(Integer id){
        return this.accountList.stream()
                .filter(account -> id.equals(account.getId()))
                .findAny();
    }

    public List<Account> search(SearchAccountVo searchAccountVo){
        return accountList.stream()
                .filter(account -> account.getCreateTime().isAfter(searchAccountVo.getStartDate())
                        && account.getCreateTime().isBefore(searchAccountVo.getEndDate()))
                .collect(Collectors.toList());
    }

    private Integer findMaxId() {
        return this.accountList.stream().mapToInt(Account::getId).max().orElse(0);
    }

}
