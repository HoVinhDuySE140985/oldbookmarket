package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepo extends JpaRepository< Wallet, Long> {
    Wallet findByUserId(Long userId);
}
