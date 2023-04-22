package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.entity.Order;
import com.example.oldbookmarket.entity.Transaction;
import com.example.oldbookmarket.entity.User;
import com.example.oldbookmarket.entity.Wallet;
import com.example.oldbookmarket.repository.OrderRepo;
import com.example.oldbookmarket.repository.TransactionRepo;
import com.example.oldbookmarket.repository.UserRepo;
import com.example.oldbookmarket.repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Autowired
    TransactionRepo transactionRepo;

//    @Scheduled(fixedDelay = 60000)
    @Scheduled(fixedDelay = 43200000)
    public void load5sForCheckOrderStatus(){
        List<Order> orderList = orderRepo.findAll();
        Transaction transaction = null;
        for (Order order: orderList) {
            User seller = userRepo.findById(order.getPost().getUser().getId()).get();
            User admin = userRepo.findById(14l).get();
            User buyer = userRepo.findById(order.getUser().getId()).get();
            //bán
            if (order.getPost().getForm().equalsIgnoreCase("Bán")){
                // dơn thành công
                if (order.getStatus().equalsIgnoreCase("complete") && order.getPaymentStatus().equalsIgnoreCase("PAID")){
                    BigDecimal amountToBePaid = order.getAmount().multiply(BigDecimal.valueOf(0.80));

                    Wallet adminWallet = walletRepo.findById(admin.getId()).get();
                    adminWallet.setAmount(adminWallet.getAmount().subtract(amountToBePaid));
                    walletRepo.save(adminWallet);

                    Wallet sellerWallet = walletRepo.findById(seller.getId()).get() ;
                    sellerWallet.setAmount(sellerWallet.getAmount().add(amountToBePaid));
                    walletRepo.save(sellerWallet);
                    transaction = Transaction.builder()
                            .type("Nhận Tiền Thanh Toán")
                            .createAt(LocalDate.now())
                            .orderCode(order.getCodeOrder())
                            .paymentMethod("Ví Của Tôi")
                            .wallet(sellerWallet)
                            .order(order)
                            .amount(amountToBePaid)
                            .build();
                    transactionRepo.save(transaction);
                    order.setPaymentStatus("PAYMENT_COMPLETED");
                    orderRepo.save(order);
                }
                //đơn hàng cancel
                if (order.getStatus().equalsIgnoreCase("cancel") && order.getPaymentStatus().equalsIgnoreCase("PAID")){
                    BigDecimal amountToBePaid = order.getAmount();

                    Wallet adminWallet = walletRepo.findById(admin.getId()).get();
                    adminWallet.setAmount(adminWallet.getAmount().subtract(amountToBePaid));
                    walletRepo.save(adminWallet);

                    Wallet buyerWallet = walletRepo.findById(buyer.getId()).get();
                    buyerWallet.setAmount(buyerWallet.getAmount().add(amountToBePaid));
                    walletRepo.save(buyerWallet);
                    transaction = Transaction.builder()
                            .type("Nhận Tiền Hoàn Trả")
                            .createAt(LocalDate.now())
                            .orderCode(order.getCodeOrder())
                            .paymentMethod("Ví Của Tôi")
                            .wallet(buyerWallet)
                            .amount(amountToBePaid)
                            .order(order)
                            .build();
                    transactionRepo.save(transaction);
                    order.setPaymentStatus("REFUND_COMPLETED");
                    order.setStatus("REFUND");
                    orderRepo.save(order);
                }
            }else {
                // trao đổi thanh cong
                if (order.getStatus().equalsIgnoreCase("Received") && order.getPaymentStatus().equalsIgnoreCase("DEPOSITED")){
                    BigDecimal amountToBePaid = order.getAmount().multiply(BigDecimal.valueOf(0.90));

                    Wallet adminWallet = walletRepo.findById(admin.getId()).get();
                    adminWallet.setAmount(adminWallet.getAmount().subtract(amountToBePaid));
                    walletRepo.save(adminWallet);

                    Wallet buyerWallet = walletRepo.findById(buyer.getId()).get();
                    buyerWallet.setAmount(buyerWallet.getAmount().add(amountToBePaid));
                    walletRepo.save(buyerWallet);
                    transaction = Transaction.builder()
                            .type("Nhận Lại Tiền Cọc")
                            .createAt(LocalDate.now())
                            .orderCode(order.getCodeOrder())
                            .paymentMethod("Ví Của Tôi")
                            .wallet(buyerWallet)
                            .order(order)
                            .amount(amountToBePaid)
                            .build();
                    transactionRepo.save(transaction);
                    order.setPaymentStatus("EXCHANGE_COMPLETED");
                    orderRepo.save(order);
                }
                // trao đổi that bai
                if (order.getStatus().equalsIgnoreCase("cancel") && order.getPaymentStatus().equalsIgnoreCase("DEPOSITED")){
                    BigDecimal amountToBePaid = order.getAmount();

                    Wallet adminWallet = walletRepo.findById(admin.getId()).get();
                    adminWallet.setAmount(adminWallet.getAmount().subtract(amountToBePaid));
                    walletRepo.save(adminWallet);

                    Wallet buyerWallet = walletRepo.findById(buyer.getId()).get();
                    buyerWallet.setAmount(buyerWallet.getAmount().add(amountToBePaid));
                    walletRepo.save(buyerWallet);
                    transaction = Transaction.builder()
                            .type("Nhận Lại Tiền Cọc")
                            .createAt(LocalDate.now())
                            .orderCode(order.getCodeOrder())
                            .paymentMethod("Ví Của Tôi")
                            .wallet(buyerWallet)
                            .amount(amountToBePaid)
                            .order(order)
                            .build();
                    transactionRepo.save(transaction);
                    order.setPaymentStatus("REFUND_COMPLETED");
                    order.setStatus("REFUND");
                    orderRepo.save(order);
                }
            }
        }
    }
}
