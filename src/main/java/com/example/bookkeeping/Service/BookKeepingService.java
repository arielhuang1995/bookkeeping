package com.example.bookkeeping.Service;

import com.example.bookkeeping.Controller.vo.AccountVo;
import com.example.bookkeeping.Controller.vo.SearchAccountVo;
import com.example.bookkeeping.Dao.AccountDaoDB;
import com.example.bookkeeping.Dao.AccountDaoMock;
import com.example.bookkeeping.Dao.AccountRepository;
import com.example.bookkeeping.Dao.IAccountDao;
import com.example.bookkeeping.Entity.Account;
import com.example.bookkeeping.Service.Dto.QueryInfoDto;
import com.example.bookkeeping.Service.Dto.ReportInfoDto;
import com.example.bookkeeping.Service.Dto.Result;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookKeepingService {

//  private final IAccountDao accountDAO = new AccountDaoDB(new AccountRepository());
  private final IAccountDao accountDAO;
//  private final AccountDaoDB accountDAO;

  public Result<Account> createAccount(AccountVo accountVo) {
    Preconditions.checkNotNull(accountVo.getAmount(), "請輸入金額");
    Preconditions.checkState(Strings.isNotBlank(accountVo.getItem()), "請輸入項目");

    Account accountEntity = new Account();
    accountEntity.setCreateTime(LocalDateTime.now());
    accountEntity.setAmount(accountVo.getAmount());
    accountEntity.setItem(accountVo.getItem());
    accountEntity.setRemark(accountVo.getRemark());


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
      a.setRemark(vo.getRemark());
//      a.setUpdateTime(LocalDateTime.now());

      accountDAO.update(a);
    });
  }

  public Result<QueryInfoDto> searchAccount(SearchAccountVo searchAccountVo) {

    List<Account> query = accountDAO.query(searchAccountVo)
            .stream().filter(x -> {
              if (Strings.isNotEmpty(searchAccountVo.getKeyWord4Item())) {
                return x.getItem().contains(searchAccountVo.getKeyWord4Item());
              }
              return true;
            }).collect(Collectors.toList());

    Result<QueryInfoDto> result = new Result<>();

    try {
      result.setData(QueryInfoDto.builder()
              .resultList(query)
              .sumOfAmount(calcSumOfAmount(query))
              .build());
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

  public ReportInfoDto report(SearchAccountVo searchAccountVo) {

    Preconditions.checkNotNull(searchAccountVo, "請輸入查詢時間");
    List<Account> accountList = accountDAO.query(searchAccountVo);

    return ReportInfoDto.builder()
            .sumOfAmount(calcSumOfAmount(accountList))
            .avgOfAmount(calcAvgOfAmount(accountList))
            .maxAmount(calcMaxAmount(accountList))
            .build();
  }

  public Account findAccountById(Integer id){
    return accountDAO.get(id).get();
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
