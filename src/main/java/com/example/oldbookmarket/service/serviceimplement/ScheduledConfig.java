package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.entity.Order;
import com.example.oldbookmarket.entity.User;
import com.example.oldbookmarket.entity.Wallet;
import com.example.oldbookmarket.repository.OrderRepo;
import com.example.oldbookmarket.repository.UserRepo;
import com.example.oldbookmarket.repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;

@Component
@Controller
public class ScheduledConfig {
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    UserRepo userRepo;

    @Autowired
    WalletRepo walletRepo;

//    @Scheduled(fixedDelay = 60000)
    @Scheduled(fixedDelay = 43200000)
    public void load5sForCheckOrderStatus(){
        List<Order> orderList = orderRepo.findAll();
        for (Order order: orderList) {
            User seller = userRepo.findById(order.getPost().getUser().getId()).get();
            User admin = userRepo.findById(13l).get();
            User buyer = userRepo.findById(order.getUser().getId()).get();
            //bán
            if (order.getPost().getForm().equalsIgnoreCase("Bán")){
                // dơn thành công
                if (order.getStatus().equalsIgnoreCase("complete") && order.getPaymentStatus().equalsIgnoreCase("PAID")){
                    Wallet sellerWallet = walletRepo.findById(seller.getId()).get();
                    Wallet adminWallet = walletRepo.findById(admin.getId()).get();
                    BigDecimal amountToBePaid = order.getAmount().multiply(BigDecimal.valueOf(0.80));
                    adminWallet.setAmount(adminWallet.getAmount().subtract(amountToBePaid));
                    System.out.println(adminWallet.getAmount());
                    walletRepo.save(adminWallet);
                    sellerWallet.setAmount(sellerWallet.getAmount().add(amountToBePaid));
                    walletRepo.save(sellerWallet);
                    order.setPaymentStatus("PAYMENT_COMPLETED");
                    orderRepo.save(order);
                }
                // đơn hàng cancel
                if (order.getStatus().equalsIgnoreCase("cancel") && order.getPaymentStatus().equalsIgnoreCase("PAID")){
                    Wallet buyerWallet = walletRepo.findById(buyer.getId()).get();
                    Wallet adminWallet = walletRepo.findById(admin.getId()).get();
                    BigDecimal amountToBePaid = order.getAmount();
                    adminWallet.setAmount(adminWallet.getAmount().subtract(amountToBePaid));
                    walletRepo.save(adminWallet);
                    buyerWallet.setAmount(buyerWallet.getAmount().add(amountToBePaid));
                    walletRepo.save(buyerWallet);
                    order.setPaymentStatus("REFUND _COMPLETED");
                    orderRepo.save(order);
                    // làm thêm phần lưu vô bảng refund
                }
            }else {
                // trao đổi
                if (order.getStatus().equalsIgnoreCase("người bán đã nhận hàng") && order.getPaymentStatus().equalsIgnoreCase("DEPOSITED")){
                    Wallet buyerWallet = walletRepo.findById(buyer.getId()).get();
                    Wallet adminWallet = walletRepo.findById(admin.getId()).get();
                    BigDecimal amountToBePaid = order.getAmount().multiply(BigDecimal.valueOf(0.90));
                    adminWallet.setAmount(adminWallet.getAmount().subtract(amountToBePaid));
                    walletRepo.save(adminWallet);
                    buyerWallet.setAmount(buyerWallet.getAmount().add(amountToBePaid));
                    walletRepo.save(buyerWallet);
                    order.setPaymentStatus("EXCHANGE_COMPLETED");
                    orderRepo.save(order);
                }
                if (order.getStatus().equalsIgnoreCase("cancel") && order.getPaymentStatus().equalsIgnoreCase("DEPOSITED")){
                    Wallet buyerWallet = walletRepo.findById(buyer.getId()).get();
                    Wallet adminWallet = walletRepo.findById(admin.getId()).get();
                    BigDecimal amountToBePaid = order.getAmount();
                    adminWallet.setAmount(adminWallet.getAmount().subtract(amountToBePaid));
                    walletRepo.save(adminWallet);
                    buyerWallet.setAmount(buyerWallet.getAmount().add(amountToBePaid));
                    walletRepo.save(buyerWallet);
                    order.setPaymentStatus("REFUND _COMPLETED");
                    orderRepo.save(order);
                    // làm thêm phần lưu vô bảng refund
                }
            }
        }
    }
}
