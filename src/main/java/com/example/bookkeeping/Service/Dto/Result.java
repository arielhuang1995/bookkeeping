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
}
