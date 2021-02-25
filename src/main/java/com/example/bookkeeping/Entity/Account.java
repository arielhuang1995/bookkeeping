package com.example.bookkeeping.Entity;

import lombok.Data;
import java.util.Date;

@Data
public class Account {

    private Integer id;
    private Double amount; // 消費金額
    private String item; // 消費項目
    private Date createTime; // 創建時間
    private Date updateTime; // 更新時間
    private String remark; // 備註
}
