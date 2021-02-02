package com.example.bookkeeping.Entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Data
public class Account {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private Double amount; // 消費金額
    private String item; // 消費項目
    private Date createTime; // 創建時間
    private Date updateTime; // 更新時間
    private String remark; // 備註

}
