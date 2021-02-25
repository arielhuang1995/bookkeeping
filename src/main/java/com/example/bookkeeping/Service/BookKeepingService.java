package com.example.bookkeeping.Service;

import com.example.bookkeeping.Controller.vo.AccountVo;
import com.example.bookkeeping.Controller.vo.SearchAccountVo;
import com.example.bookkeeping.Dao.AccountDao;
import com.example.bookkeeping.Entity.Account;
import com.example.bookkeeping.Service.Dto.ReportInfoDto;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookKeepingService {

  // TODO:DataAccessObject(DAO) 封裝
  private final AccountDao accountDAO;

  public void createAccount(AccountVo accountVo) {
    Preconditions.checkState(null != accountVo.getAmount(), "請輸入金額");
    Preconditions.checkState(Strings.isNotBlank(accountVo.getItem()), "請輸入項目");

    Account accountEntity = new Account();
    accountEntity.setCreateTime(new Date());
    accountEntity.setAmount(accountVo.getAmount());
    accountEntity.setItem(accountVo.getItem());

    accountDAO.add(accountEntity);
  }

  public void deleteAccount(Integer id) {
    accountDAO.delete(id);
  }

  public void updateAccount(AccountVo vo) {
    Account accountFromDB = accountDAO.get(vo.getId());
    Preconditions.checkState(accountFromDB != null, "查無此筆資料！");

    accountFromDB.setItem(vo.getItem());
    accountFromDB.setAmount(vo.getAmount());
    accountFromDB.setUpdateTime(new Date());

    accountDAO.update(accountFromDB);
  }

  public Map<String, Object> searchAccount(SearchAccountVo searchAccountVo) {
    // TODO 重構 searchAccount,抽出searchAccount、report 共用的部份
    Map<String, Object> response = new HashMap<>();

    // TODO 把檢查＆給預設值抽去vo
    if (null == searchAccountVo.getStartDate() && null == searchAccountVo.getEndDate()) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(new Date().getTime());
      calendar.set(Calendar.DAY_OF_MONTH, 1);
      searchAccountVo.setStartDate(calendar.getTime());
      calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
      searchAccountVo.setEndDate(calendar.getTime());
    }
    Preconditions.checkState(
        null != searchAccountVo.getStartDate() && null != searchAccountVo.getEndDate(),
        "請輸入完整時間區間");
    List<Account> result = accountDAO.search(searchAccountVo);
    if (Strings.isNotEmpty(searchAccountVo.getKeyWord4Item())) {
      result.stream()
          .filter(account -> account.getItem().contains(searchAccountVo.getKeyWord4Item()));
    }
    Double sumOfAmount = calSumOfAmount(result);

    response.put("accountList", result);
    response.put("sumOfAccount", sumOfAmount);

    return response;
  }

  public ReportInfoDto report(SearchAccountVo searchAccountVo) {
    Preconditions.checkState(null != searchAccountVo, "請輸入查詢時間");
    List<Account> accountList = accountDAO.search(searchAccountVo);
    Double sumOfAmount = calSumOfAmount(accountList);
    BigDecimal bdSumOfAmount = new BigDecimal(sumOfAmount); //TODO 處理空值情況
    BigDecimal bdAccountNum = new BigDecimal(accountList.size());
    Double avgOfAmount = bdSumOfAmount.divide(bdAccountNum, 2).doubleValue();
    Double maxAmount =
        accountList.stream()
            .collect(Collectors.maxBy(Comparator.comparing(Account::getAmount)))
            .get()
            .getAmount()
            .doubleValue();

    // TODO：請思考如何不用remove的方式來實作這邊..

    return ReportInfoDto.builder()
            .sumOfAmount(sumOfAmount)
            .avgOfAmount(avgOfAmount)
            .maxAmount(maxAmount)
            .build();
  }

  private Double calSumOfAmount(List<Account> accountList){
    Double sumOfAmount = 0D;
    for (Account account : accountList) {
      sumOfAmount = Double.sum(sumOfAmount, account.getAmount());
    }
    return sumOfAmount;
  }
}
