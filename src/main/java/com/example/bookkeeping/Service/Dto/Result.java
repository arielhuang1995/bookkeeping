package com.example.bookkeeping.Service.Dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Result<T> {

  private T data;

  private Boolean success = false;

  private String errMsg;

  private String errCode;

  public String toViewErrorMsg() {

    if (this.errCode.equals("1")) {
      return "Please Contact The Administrator";
    }

    if (this.errCode.equals("2")) {
      return "System Error";
    }

    return "OK";
  }
}
