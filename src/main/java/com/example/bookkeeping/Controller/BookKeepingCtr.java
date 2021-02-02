package com.example.bookkeeping.Controller;

import com.example.bookkeeping.Entity.Account;
import com.example.bookkeeping.Entity.vo.AccountVo;
import com.example.bookkeeping.Entity.vo.SearchAccountVo;
import com.example.bookkeeping.Service.BookKeepingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookKeeping")
public class BookKeepingCtr {

  private final BookKeepingService bookKeepingService;

  @PostMapping("add")
  public String add(@RequestBody AccountVo account) {
    bookKeepingService.createAccount(account);
    return "Success!";
  }

  @PostMapping("update")
  public String update(@RequestBody Account account) {
    bookKeepingService.updateAccount(account);
    return "Success!";
  }

  @GetMapping("delete")
  public String delete(@RequestParam("id") Integer id) {
    bookKeepingService.deleteAccount(id);
    return "Success!";
  }

  @PostMapping("search")
  public Map<String, Object> search(@RequestBody(required = false) SearchAccountVo searchAccountVo) {
      if(searchAccountVo == null) searchAccountVo = new SearchAccountVo();
      return bookKeepingService.searchAccount(searchAccountVo);
  }

  @PostMapping("report")
  public Map<String, Object> report(@RequestBody SearchAccountVo searchAccountVo) {
    return bookKeepingService.report(searchAccountVo);
  }
}
