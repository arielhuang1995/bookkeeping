package com.example.bookkeeping.Entity;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor //無參建構子
@Entity
@Table(name = "account")
//@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private Double amount; // 消費金額
    private String item; // 消費項目
    @Column(name = "create_time")
    private LocalDateTime createTime = LocalDateTime.now(); // 創建時間
    @Column(name = "update_time")
    private LocalDateTime updateTime = LocalDateTime.now(); // 更新時間
    private String remark; // 備註

//    public  Account(){ }

    public Account(Integer id,Double amount,String item,String remark){
        this.id = id;
        this.amount = amount;
        this.item = item;
        this.remark = remark;
    }

//    public Account create(Integer id,Double amount,String item,String remark){
//        return Account.builder()
//                .id(id)
//                .amount(amount)
//                .item(item)
//                .remark(remark)
//                .build();
//    }
}
