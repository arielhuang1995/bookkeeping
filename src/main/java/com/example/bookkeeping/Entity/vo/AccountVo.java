package com.example.bookkeeping.Entity.vo;

import lombok.Data;

//todo:VO搬家
@Data
public class AccountVo {
    private Double amount; // 消費金額
    private String item; // 消費項目
    private String remark; // 備註
}
