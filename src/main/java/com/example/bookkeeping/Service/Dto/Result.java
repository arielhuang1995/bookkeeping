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

    public String toViewErrorMsg(){
        if (errCode=="1") {
            return "請聯絡管理員";
        }

        if (this.errCode.equals("2")) {
            return "系統爆炸，請登出!!";
        }
    }
}
