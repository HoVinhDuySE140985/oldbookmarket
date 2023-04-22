package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.response.TransactionDTO.TransResponseDTO;
import com.example.oldbookmarket.entity.Transaction;
import com.example.oldbookmarket.entity.Wallet;
import com.example.oldbookmarket.repository.TransactionRepo;
import com.example.oldbookmarket.repository.WalletRepo;
import com.example.oldbookmarket.service.serviceinterface.TransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransServiceImpl implements TransService {

    @Autowired
    WalletRepo walletRepo;

    @Autowired
    TransactionRepo transactionRepo;

    @Override
    public List<TransResponseDTO> getAllTransaction(Long userId) {
        List<TransResponseDTO> transResponseDTOS = new ArrayList<>();
        TransResponseDTO responseDTO = null;
        try {
            Wallet wallet = walletRepo.findByUserId(userId);
            List<Transaction> transactions = transactionRepo.findAllByWallet_WalletId(wallet.getWalletId());
            for (Transaction transaction: transactions) {
                responseDTO = TransResponseDTO.builder()
                        .transId(transaction.getTransId())
                        .type(transaction.getType())
                        .amount(transaction.getAmount())
                        .createAt(transaction.getCreateAt())
                        .orderCode(transaction.getOrderCode())
                        .build();
                transResponseDTOS.add(responseDTO);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return transResponseDTOS;
    }
}
