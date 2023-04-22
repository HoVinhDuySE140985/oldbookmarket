package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByWallet_WalletId(Long walletId);
}
