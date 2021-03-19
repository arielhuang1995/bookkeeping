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


  @PostMapping("add")
  public Result<Account> add(@RequestBody AccountVo account) {
    return bookKeepingService.createAccount(account);
  }

  @PostMapping("update")
  public String update(@RequestBody AccountVo account) {
    bookKeepingService.updateAccount(account);
    return "Success!";
  }

  @GetMapping("delete")
  public String delete(@RequestParam("id") Integer id) {
    bookKeepingService.deleteAccount(id);
    return "Success!";
  }

  @PostMapping("search")
  public Result<QueryInfoDto> search(@RequestBody(required = false) SearchAccountVo searchAccountVo) {
      if(searchAccountVo == null)
        searchAccountVo = new SearchAccountVo();

      return bookKeepingService.searchAccount(searchAccountVo);
  }

  @PostMapping("report")
  public ReportInfoDto report(@RequestBody SearchAccountVo searchAccountVo) {
    return bookKeepingService.report(searchAccountVo);
  }
}
