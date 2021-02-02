package com.example.bookkeeping.Service;

import com.example.bookkeeping.Entity.Account;
import com.example.bookkeeping.Entity.vo.AccountVo;
import com.example.bookkeeping.Entity.vo.SearchAccountVo;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookKeepingService {

  //TODO:DataAccessObject(DAO) 封裝
  private List<Account> accountList = new ArrayList<>();

  public void createAccount(AccountVo account) {
    Preconditions.checkState(null != account.getAmount(), "請輸入金額");
    Preconditions.checkState(Strings.isNotBlank(account.getItem()), "請輸入項目");

    Account accountEntity = new Account();
    accountEntity.setId(findMaxId() + 1);
    accountEntity.setCreateTime(new Date());
    accountEntity.setAmount(account.getAmount());//可能轉美金
    accountEntity.setItem(account.getItem());//


    accountList.add(accountEntity);
  }

  public void deleteAccount(Integer id) {
    accountList.forEach(
        account -> {
          if (account.getId().equals(id)) {
            accountList.remove(account);
          }
        });

//    Optional<Account> account = accountList.stream()
//            .filter(x -> x.getId().equals(id)).findFirst();
//
//    Account account1 = account.get();
//
//    accountList.remove(account1);

  }

  public void updateAccount(Account newAccount) {
    accountList.forEach(
            account -> {
              if (account.getId().equals(newAccount.getId())) {
                Integer index = accountList.indexOf(account);
                newAccount.setUpdateTime(new Date());
                accountList.set(index, newAccount);
              }
            });
  }

//  public void updateAccount_2(String id,AccountVo vo){
//    //調用dao get data
//    Account accountFromDB = dao.getAccountById(id);
//    accountFromDB.setItem();
//    accountFromDB.setUpdateTime();
//    accountFromDB.setAmount();
//
//    dao.save(accountFromDB);
//  }

  public Map<String, Object> searchAccount(SearchAccountVo searchAccountVo) {
    //TODO 重構 searchAccount,抽出searchAccount、report 共用的部份
    Map<String, Object> response = new HashMap<>();
    Double sumOfAmount = Double.NaN;
    if (null == searchAccountVo.getStartDate() && null == searchAccountVo.getEndDate()) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(new Date().getTime());
      calendar.set(Calendar.DAY_OF_MONTH, 1);
      searchAccountVo.setStartDate(calendar.getTime());
      calendar.set(
          Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
      searchAccountVo.setEndDate(calendar.getTime());
    }
    Preconditions.checkState(
        null != searchAccountVo.getStartDate() && null != searchAccountVo.getEndDate(),
        "請輸入完整時間區間");

    List<Account> result =
        accountList.stream()
            .filter(
                account ->
                    account.getCreateTime().after(searchAccountVo.getStartDate())
                        && account.getCreateTime().before(searchAccountVo.getEndDate()))
            .collect(Collectors.toList());
    if (Strings.isNotEmpty(searchAccountVo.getKeyWord4Item())) {
      result.stream()
          .filter(account -> account.getItem().contains(searchAccountVo.getKeyWord4Item()));
    }
    for (Account account : result) {
      sumOfAmount = Double.sum(sumOfAmount, account.getAmount());
    }

    response.put("accountList", result);
    response.put("sumOfAccount", sumOfAmount);

    return response;
  }

  public Map<String, Object>  report(SearchAccountVo searchAccountVo) {
    Preconditions.checkState(null != searchAccountVo, "請輸入查詢時間");
    Map<String, Object> response = searchAccount(searchAccountVo);

    //能不強轉，就不要強轉
    List<Account> accountList = (List<Account>) response.get("accountList");
    BigDecimal bdSumOfAmount = new BigDecimal(response.get("sumOfAmount").toString());
    BigDecimal bdAccountNum = new BigDecimal(accountList.size());
    Double avgOfAmount = bdSumOfAmount.divide(bdAccountNum, 2).doubleValue();
    Double maxAmount =
        accountList.stream()
            .collect(Collectors.maxBy(Comparator.comparing(Account::getAmount)))
            .get()
            .getAmount()
            .doubleValue();

    //TODO：請思考如何不用remove的方式來實作這邊..
    response.remove("accountList");

    //這個是弱型別的寫法
    //請採用ReportInfo物件封裝
    response.put("avgOfAmount", avgOfAmount);
    response.put("maxAmount", maxAmount);


//    return ReportInfo.builder()
//            .accountList(new LinkedList<>())
//            .avgOfAmount(0D)
//            .build();

    return response;
  }

  @Data
  @Builder
  static class ReportInfo{
    private List<String> accountList;
    private Double avgOfAmount;
  }

  private Integer findMaxId() {
    if (accountList.size() != 0) {
      return accountList.stream()
          .collect(Collectors.maxBy(Comparator.comparing(Account::getId)))
          .get()
          .getId();
    } else {
      return 0;
    }


//    return this.accountList.stream().mapToInt(Account::getId).max().orElse(0);
  }
}
