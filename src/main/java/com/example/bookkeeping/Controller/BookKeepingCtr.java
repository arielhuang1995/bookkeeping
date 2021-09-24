package com.example.bookkeeping.Controller;

import com.example.bookkeeping.Controller.vo.AccountVo;
import com.example.bookkeeping.Controller.vo.SearchAccountVo;
import com.example.bookkeeping.Entity.Account;
import com.example.bookkeeping.Service.BookKeepingService;
import com.example.bookkeeping.Service.Dto.QueryInfoDto;
import com.example.bookkeeping.Service.Dto.ReportInfoDto;
import com.example.bookkeeping.Service.Dto.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookKeeping")
public class BookKeepingCtr {

  private final BookKeepingService bookKeepingService;


  @CrossOrigin
  @PostMapping("add")
  public Result<Account> add(@RequestBody AccountVo account) {
    return bookKeepingService.createAccount(account);
  }

  @CrossOrigin
  @PostMapping("update")
  public String update(@RequestBody AccountVo account) {
    bookKeepingService.updateAccount(account);
    return "Success!";
  }

  @CrossOrigin
  @GetMapping("delete")
  public String delete(@RequestParam("id") Integer id) {
    bookKeepingService.deleteAccount(id);
    return "Success!";
  }

  @CrossOrigin
  @PostMapping("search")
  public Result<QueryInfoDto> search(@RequestBody(required = false) SearchAccountVo searchAccountVo) {
    searchAccountVo = searchAccountVo == null ? new SearchAccountVo() : searchAccountVo;
    searchAccountVo.validate();
    return bookKeepingService.searchAccount(searchAccountVo);
  }

  @CrossOrigin
  @PostMapping("report")
  public ReportInfoDto report(@RequestBody(required = false) SearchAccountVo searchAccountVo) {
    searchAccountVo = searchAccountVo == null ? new SearchAccountVo() : searchAccountVo;
    searchAccountVo.validate();
    return bookKeepingService.report(searchAccountVo);
  }

  @CrossOrigin
  @GetMapping("getOne")
  public Account getOne(@RequestParam("id") Integer id) {
    return bookKeepingService.findAccountById(id);
  }
}
