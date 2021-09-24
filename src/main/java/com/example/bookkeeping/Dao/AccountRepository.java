package com.example.bookkeeping.Dao;

import com.example.bookkeeping.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

  List<Account> findByCreateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
}
