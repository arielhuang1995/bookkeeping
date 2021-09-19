package com.example.bookkeeping.Dao;

import com.example.bookkeeping.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

  //    @Query(value = "Select * From account Where " +
  //            "create_time >= :startDate And  create_time <= :endDate ", nativeQuery = true)
  //    List<Account> findByCondition (@Param("startDate") LocalDateTime startDate,
  //                                   @Param("endDate") LocalDateTime endDate);


  List<Account> findByCreateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
}
