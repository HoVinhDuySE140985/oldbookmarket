package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.NotiRequestDTO.PnsRequest;
import com.example.oldbookmarket.dto.response.momoDTO.MomoResponse;
import com.example.oldbookmarket.dto.response.walletDTO.WalletResponseDTO;
import com.example.oldbookmarket.entity.PostNotification;
import com.example.oldbookmarket.entity.Transaction;
import com.example.oldbookmarket.entity.User;
import com.example.oldbookmarket.entity.Wallet;
import com.example.oldbookmarket.repository.TransactionRepo;
import com.example.oldbookmarket.repository.UserRepo;
import com.example.oldbookmarket.repository.WalletRepo;
import com.example.oldbookmarket.service.serviceinterface.FcmService;
import com.example.oldbookmarket.service.serviceinterface.PaymentService;
import com.example.oldbookmarket.service.serviceinterface.WalletService;
import com.example.oldbookmarket.shared.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    WalletRepo walletRepo;

    @Autowired
    PaymentService paymentService;

    @Autowired
    UserRepo userRepo;

    @Autowired
    TransactionRepo transactionRepo;

    @Autowired
    FcmService fcmService;

    @Override
    public ResponseEntity<MomoResponse> rechargeIntoWalletMoMo(Long userId, BigDecimal amount) {
        ResponseEntity<MomoResponse> response = null;
        try {
            String orderCode = Utilities.randomAlphaNumeric(10);
            response = paymentService.getPaymentMomoV1(orderCode, userId, 222234453L, amount, "null", "null", "null", "NẠP TIỀN");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public ResponseEntity<MomoResponse> rechargeIntoWallet(Long userId, BigDecimal depositAmount, String orderCode) {
        ResponseEntity<MomoResponse> response = null;
        try {
            Wallet wallet = walletRepo.findById(userId).get();
            wallet.setAmount(wallet.getAmount().add(depositAmount));
            walletRepo.save(wallet);
            System.out.println(wallet.getAmount());
            Transaction transaction = Transaction.builder()
                    .type("Nạp Tiền")
                    .paymentMethod("Ví MoMo")
                    .orderCode(orderCode)
                    .wallet(wallet)
                    .amount(depositAmount)
                    .createAt(LocalDate.now())
                    .build();
            transactionRepo.save(transaction);
            List<String> fcmKey = new ArrayList<>();
            User user = userRepo.findById(userId).get();
            if (!user.getFcmKey().isEmpty() && user.getFcmKey() != null) {
                fcmKey.add(user.getFcmKey());
            }
            if (!fcmKey.isEmpty() || fcmKey.size() > 0) { // co key
                // pushnoti
                PnsRequest pnsRequest = new PnsRequest(fcmKey, "Cuon sach da duoc duyet",
                        "Hay nhanh chong xem chi tiet cuon sach ban da dang ki");
                fcmService.pushNotification(pnsRequest);
            }
        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return walletResponseDTO;
    }
}
