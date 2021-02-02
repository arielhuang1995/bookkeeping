package com.example.bookkeeping.Service;

import com.example.bookkeeping.Entity.Account;
import com.example.bookkeeping.Entity.vo.SearchAccountVo;
import com.google.common.base.Preconditions;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookKeepingService {

  private List<Account> accountList = new ArrayList<>();

  public void createAccount(Account account) {
    Preconditions.checkState(null != account.getAmount(), "請輸入金額");
    Preconditions.checkState(Strings.isNotBlank(account.getItem()), "請輸入項目");
    account.setId(findMaxId() + 1);
    account.setCreateTime(new Date());
    accountList.add(account);
  }

  public void deleteAccount(Integer id) {
    accountList.forEach(
        account -> {
          if (account.getId().equals(id)) {
            accountList.remove(account);
          }
        });
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

  public Map<String, Object> searchAccount(SearchAccountVo searchAccountVo) {
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

  public Map<String, Object> report(SearchAccountVo searchAccountVo) {
    Preconditions.checkState(null != searchAccountVo, "請輸入查詢時間");
    Map<String, Object> response = searchAccount(searchAccountVo);
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
    response.remove("accountList");
    response.put("avgOfAmount", avgOfAmount);
    response.put("maxAmount", maxAmount);
    return response;
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
  }
}
