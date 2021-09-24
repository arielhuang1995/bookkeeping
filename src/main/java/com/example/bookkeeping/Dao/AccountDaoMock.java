package com.example.bookkeeping.Dao;

import com.example.bookkeeping.Controller.vo.SearchAccountVo;
import com.example.bookkeeping.Entity.Account;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(value = "app.api.factory", havingValue = "monk")
public class AccountDaoMock implements IAccountDao {

    private final List<Account> accountList = new ArrayList<Account>(Arrays.asList(
            new Account(1,10.0,"Apple","Bought At Super Market"),
            new Account(2,20.0,"Orange","Buy At Store"),
            new Account(3,30.0,"Banana","Buy At Street Vendor")
    ));

    @Override
    public void add(Account account){
        account.setId(findMaxId() + 1);
        accountList.add(account);
    }

    @Override
    public void delete(Integer id){
        Optional<Account> accountOpt = this.get(id);
        accountOpt.ifPresent(this.accountList::remove);
    }

    @Override
    public void update(Account newAccount){
        Optional<Account> account = this.get(newAccount.getId());
        account.ifPresent(a -> {
            int accountIndex = accountList.indexOf(a);
            newAccount.setUpdateTime(LocalDateTime.now());
            accountList.set(accountIndex, newAccount);
            System.out.println(newAccount);
        });
    }

    @Override
    public Optional<Account> get(Integer id){
        return this.accountList.stream()
                .filter(account -> id.equals(account.getId()))
                .findAny();
    }

    @Override
    public List<Account> query(SearchAccountVo searchAccountVo){
        return accountList.stream()
                .filter(account -> account.getCreateTime().isAfter(searchAccountVo.getStartDate())
                        && account.getCreateTime().isBefore(searchAccountVo.getEndDate()))
                .collect(Collectors.toList());
    }

    private Integer findMaxId() {
        return this.accountList.stream().mapToInt(Account::getId).max().orElse(0);
    }

}
