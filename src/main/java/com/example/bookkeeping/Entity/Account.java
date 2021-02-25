package com.example.bookkeeping.Entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Account {

    private Integer id;
    private Double amount; // 消費金額
    private String item; // 消費項目
    private LocalDateTime createTime = LocalDateTime.now(); // 創建時間
    private LocalDateTime updateTime; // 更新時間
    private String remark; // 備註
}
