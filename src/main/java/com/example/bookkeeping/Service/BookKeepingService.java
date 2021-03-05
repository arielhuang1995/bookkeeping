package com.example.bookkeeping.Service;

import com.example.bookkeeping.Controller.vo.AccountVo;
import com.example.bookkeeping.Controller.vo.SearchAccountVo;
import com.example.bookkeeping.Dao.AccountDaoDB;
import com.example.bookkeeping.Dao.IAccountDao;
import com.example.bookkeeping.Entity.Account;
import com.example.bookkeeping.Service.Dto.ReportInfoDto;
import com.example.bookkeeping.Service.Dto.Result;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookKeepingService {

  private final IAccountDao accountDAO = new AccountDaoDB();

  public Result<Account> createAccount(AccountVo accountVo) {
    Preconditions.checkNotNull(accountVo.getAmount(), "請輸入金額");
    Preconditions.checkState(Strings.isNotBlank(accountVo.getItem()), "請輸入項目");

    Account accountEntity = new Account();
    accountEntity.setCreateTime(LocalDateTime.now());
    accountEntity.setAmount(accountVo.getAmount());
    accountEntity.setItem(accountVo.getItem());


    Result<Account> result = new Result<>();

    try {
      accountDAO.add(accountEntity);
      result.setData(accountEntity);
      result.setSuccess(true);

      return result;
    }
    catch (Exception e)
    {
      result.setSuccess(false);
      result.setErrMsg(e.getMessage());
      result.setErrCode("500");

      return result;
    }

  }

  public void deleteAccount(Integer id) {
    accountDAO.delete(id);
  }

  public void updateAccount(AccountVo vo) {

    Optional<Account> accountFromDB = accountDAO.get(vo.getId());
//    Preconditions.checkState(accountFromDB.isPresent(), "查無此筆資料！");

    accountFromDB.ifPresent(a -> {
      a.setItem(vo.getItem());
      a.setAmount(vo.getAmount());
//      a.setUpdateTime(LocalDateTime.now());

      accountDAO.update(a);
    });
  }

  public Map<String, Object> searchAccount(SearchAccountVo searchAccountVo) {

    // vo有給定default，這些檢查應該都可以拿掉
//    if (null == searchAccountVo.getStartDate() && null == searchAccountVo.getEndDate()) {
//      Calendar calendar = Calendar.getInstance();
//      calendar.setTimeInMillis(new Date().getTime());
//      calendar.set(Calendar.DAY_OF_MONTH, 1);
//      searchAccountVo.setStartDate(calendar.getTime());
//
//      calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//      searchAccountVo.setEndDate(calendar.getTime());
//    }


//    Preconditions.checkState(
//            null != searchAccountVo.getStartDate()
//                    && null != searchAccountVo.getEndDate(),
//            "請輸入完整時間區間");


    List<Account> result = accountDAO.query(searchAccountVo);
    if (Strings.isNotEmpty(searchAccountVo.getKeyWord4Item())) {
      result.stream()
          .filter(account -> account.getItem().contains(searchAccountVo.getKeyWord4Item()));
    }

    Map<String, Object> response = new HashMap<>();//羅輯層不應有response的概念
    response.put("accountList", result);
    response.put("sumOfAccount", calcSumOfAmount(result));

    return response;
  }

  public ReportInfoDto report(SearchAccountVo searchAccountVo) {

    Preconditions.checkNotNull(searchAccountVo, "請輸入查詢時間");
    List<Account> accountList = accountDAO.query(searchAccountVo);

    return ReportInfoDto.builder()
            .sumOfAmount(calcSumOfAmount(accountList))
            .avgOfAmount(calcAvgOfAmount(accountList))
            .maxAmount(calcMaxAmount(accountList))
            .build();
  }

  private double calcAvgOfAmount(List<Account> accountList) {
    BigDecimal bdSumOfAmount = BigDecimal.valueOf(calcSumOfAmount(accountList));//TODO 處理空值情況
    BigDecimal bdAccountNum = new BigDecimal(accountList.size());

    return bdSumOfAmount.divide(bdAccountNum, 2).doubleValue();
  }

  private double calcMaxAmount(List<Account> accountList) {
    return accountList.stream().mapToDouble(Account::getAmount).max().getAsDouble();
  }

  private Double calcSumOfAmount(List<Account> accountList){
    return accountList.stream().mapToDouble(Account::getAmount).sum();
  }
}
