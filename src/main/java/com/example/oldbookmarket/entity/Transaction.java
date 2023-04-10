package com.example.oldbookmarket.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tranId;
    private LocalDate createAt;
    private BigDecimal amount;
    private Long orderId; // nối vô bảng order
    private String transactionType;
    private String paymentMethod;
    private Long walletId; // nối vô bảng wallet
}
