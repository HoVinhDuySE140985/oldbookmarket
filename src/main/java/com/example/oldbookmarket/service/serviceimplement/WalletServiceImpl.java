package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.response.momoDTO.MomoResponse;
import com.example.oldbookmarket.dto.response.walletDTO.WalletResponseDTO;
import com.example.oldbookmarket.entity.User;
import com.example.oldbookmarket.entity.Wallet;
import com.example.oldbookmarket.repository.UserRepo;
import com.example.oldbookmarket.repository.WalletRepo;
import com.example.oldbookmarket.service.serviceinterface.PaymentService;
import com.example.oldbookmarket.service.serviceinterface.WalletService;
import com.example.oldbookmarket.shared.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    WalletRepo walletRepo;

    @Autowired
    PaymentService paymentService;

    @Override
    public ResponseEntity<MomoResponse> rechargeIntoWalletMoMo(Long userId, BigDecimal amount) {
        ResponseEntity<MomoResponse> response = null;
        try {
            String orderCode = Utilities.randomAlphaNumeric(10);
            response = paymentService.getPaymentMomoV1(orderCode,userId,222234453L,amount,"null","null","null","NẠP TIỀN");
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }    @Override
    public ResponseEntity<MomoResponse> rechargeIntoWallet(Long userId, BigDecimal depositAmount) {
        ResponseEntity<MomoResponse> response = null;
        System.out.println(userId);
        try {
                Wallet wallet = walletRepo.findById(userId).get();
                wallet.setAmount(wallet.getAmount().add(depositAmount));
                walletRepo.save(wallet);
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public WalletResponseDTO viewWallet(Long userId) {
        WalletResponseDTO walletResponseDTO = null;
        try {
            Wallet wallet = walletRepo.findById(userId).get();
            walletResponseDTO = WalletResponseDTO.builder()
                    .amount(wallet.getAmount())
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return walletResponseDTO;
    }
}
