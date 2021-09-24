package com.example.bookkeeping.Service;

import com.example.bookkeeping.Controller.vo.AccountVo;
import com.example.bookkeeping.Controller.vo.SearchAccountVo;
import com.example.bookkeeping.Dao.IAccountDao;
import com.example.bookkeeping.Entity.Account;
import com.example.bookkeeping.Service.Dto.QueryInfoDto;
import com.example.bookkeeping.Service.Dto.ReportInfoDto;
import com.example.bookkeeping.Service.Dto.Result;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookKeepingService {

  private final IAccountDao accountDAO;

  public Result<Account> createAccount(AccountVo accountVo) {
    Preconditions.checkNotNull(accountVo.getAmount(), "Please Enter Amount");
    Preconditions.checkState(Strings.isNotBlank(accountVo.getItem()), "Please Enter Item");

    Account accountEntity = new Account();
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
    this.findAccountById(id);
    accountDAO.delete(id);
  }

  public void updateAccount(AccountVo vo) {

    Optional<Account> accountFromDB = accountDAO.get(vo.getId());

    accountFromDB.ifPresent(a -> {
      a.setItem(vo.getItem());
      a.setAmount(vo.getAmount());
      a.setRemark(vo.getRemark());
      a.setUpdateTime(LocalDateTime.now());

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
    List<Account> accountList = accountDAO.query(searchAccountVo);

    if(CollectionUtils.isEmpty(accountList)) {
      log.info("No Data");
      return ReportInfoDto.builder()
              .sumOfAmount(0d)
              .avgOfAmount(0d)
              .maxAmount(0d)
              .build();
    }

    return ReportInfoDto.builder()
            .sumOfAmount(calcSumOfAmount(accountList))
            .avgOfAmount(calcAvgOfAmount(accountList))
            .maxAmount(calcMaxAmount(accountList))
            .build();
  }

  public Account findAccountById(Integer id){
    Optional<Account> account = accountDAO.get(id);
    Preconditions.checkState(account.isPresent(),"ID Not Exit!");
    return account.get();
  }

  private double calcAvgOfAmount(List<Account> accountList) {
    BigDecimal bdSumOfAmount = BigDecimal.valueOf(calcSumOfAmount(accountList));
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
