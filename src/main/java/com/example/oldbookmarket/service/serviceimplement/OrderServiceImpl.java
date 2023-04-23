package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.orderDTO.AddOrderRequestDTO;
import com.example.oldbookmarket.dto.response.bookDTO.BookPendingResponseDTO;
import com.example.oldbookmarket.dto.response.momoDTO.MomoResponse;
import com.example.oldbookmarket.dto.response.orderDTO.OrderHistoryResponseDTO;
import com.example.oldbookmarket.dto.response.orderDTO.OrderResponseDTO;
import com.example.oldbookmarket.dto.response.orderDTO.RevenueResponseDTO;
import com.example.oldbookmarket.entity.*;
import com.example.oldbookmarket.repository.*;
import com.example.oldbookmarket.service.serviceinterface.OrderService;
import com.example.oldbookmarket.service.serviceinterface.PaymentService;
import com.example.oldbookmarket.shared.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    PostRepo postRepo;

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    WalletRepo walletRepo;

    @Autowired
    TransactionRepo transactionRepo;

    @Autowired
    PaymentService paymentService;

    @Autowired
    BookRepo bookRepo;

    @Override
    @Transactional
    public ResponseEntity<MomoResponse> createNewOrderWithMomo(Long postId, Long userId, BigDecimal amount, String paymentMethod, String note, String shipAddress) {
        ResponseEntity<MomoResponse> response = null;
        try {
            String orderCode = Utilities.randomAlphaNumeric(10);
            response = paymentService.getPaymentMomoV1(orderCode, postId, userId, amount, paymentMethod, note, shipAddress, "Thanh Toán");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public ResponseEntity<MomoResponse> createNewOrder(Long postId, Long userId, BigDecimal amount, String paymentMethod, String note, String shipAddress, String orderCode) {
        ResponseEntity<MomoResponse> response = null;
        User user = userRepo.findById(userId).get();
        Post post = postRepo.findById(postId).get();
        try {
            post.setPostStatus("deactivate");
            postRepo.save(post);
            if (post.getForm().equalsIgnoreCase("bán")) {
                Order order = Order.builder()
                        .user(user)
                        .shipAddress(shipAddress)
                        .orderDate(LocalDate.now())
                        .post(post)
                        .amount(post.getPrice())
                        .note(note)
                        .codeOrder(orderCode)
                        .paymentMethod("VÍ MOMO")
                        .deliveryMethod("Khách Hàng Tự Thỏa Thuận")
                        .status("WaitingForConfirmation")
                        .paymentStatus("PAID")
                        .build();
                orderRepo.save(order);
                Transaction transaction = Transaction.builder()
                        .createAt(LocalDate.now())
                        .type("Thanh Toán")
                        .paymentMethod("Ví MoMo")
                        .orderCode(orderCode)
                        .wallet(walletRepo.findByUserId(userId))
                        .amount(amount)
                        .build();
                transactionRepo.save(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    @Transactional
    public OrderResponseDTO createNewOrderWithMyWallet(AddOrderRequestDTO addOrderRequestDTO) {
        OrderResponseDTO orderResponseDTO = null;
        Transaction transaction = null;
        String orderCode = Utilities.randomAlphaNumeric(10);
        User user = userRepo.findById(addOrderRequestDTO.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Post post = postRepo.findById(addOrderRequestDTO.getPostId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        post.setPostStatus("deactivate");
        postRepo.save(post);
        if (post.getForm().equalsIgnoreCase("bán")) {
            // kiểm tra tiền trong ví người mua neu ko du thi gui thong bao nap tien va tiep tuc
            // neu du tien thi tao don hang va tru tien cua nguoi mua chuyen vao tk admin
            // sau khi ket giao dich chuyen tien tu TK admin vao tai khoan nguoi ban - hoa hong
            Wallet walletBuyer = walletRepo.findByUserId(user.getId());
            if (walletBuyer.getAmount().compareTo(post.getPrice()) < 0) {
                throw new ResponseStatusException(HttpStatus.valueOf(400), "VÍ CỦA BẠN KHÔNG ĐỦ TIỀN VUI LONG NẠP TIỀN VÀ THỬ LẠI");
            } else {
                Order order = Order.builder()
                        .user(user)
                        .shipAddress(addOrderRequestDTO.getShipAddress())
                        .orderDate(LocalDate.now())
                        .post(post)
                        .amount(post.getPrice())
                        .note(addOrderRequestDTO.getNote())
                        .paymentMethod("VÍ CỦA TÔI")
                        .deliveryMethod("Khách Hàng Tự Thỏa Thuận")
                        .status("WaitingForConfirmation")
                        .codeOrder(orderCode)
                        .paymentStatus("PAID")
                        .build();
                order = orderRepo.save(order);
                orderResponseDTO = OrderResponseDTO.builder()
                        .orderId(order.getId())
                        .postId(order.getPost().getId())
                        .userId(order.getUser().getId())
                        .shipAddress(order.getShipAddress())
                        .orderDate(order.getOrderDate())
                        .amount(order.getAmount())
                        .note(order.getNote())
                        .paymentMethod(order.getPaymentMethod())
                        .deliveryMethod(order.getDeliveryMethod())
                        .status(order.getStatus())
                        .paymentStatus(order.getPaymentStatus())
                        .build();
                // tru tien trong vi va luu lai
                walletBuyer.setAmount(walletBuyer.getAmount().subtract(order.getAmount()));
                walletRepo.save(walletBuyer);
                transaction = Transaction.builder()
                        .createAt(LocalDate.now())
                        .type("Thanh Toán")
                        .paymentMethod("Ví Của Tôi")
                        .orderCode(orderCode)
                        .wallet(walletBuyer)
                        .amount(order.getAmount())
                        .build();
                transactionRepo.save(transaction);
                // cong tien vao vi admin
                User admin = userRepo.findUserByRole_Id(1L);
                Wallet adminWallet = walletRepo.findById(admin.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
                adminWallet.setAmount(adminWallet.getAmount().add(order.getAmount()));
                walletRepo.save(adminWallet);
                transaction = Transaction.builder()
                        .createAt(LocalDate.now())
                        .type("Admin Nhận Thanh Toán")
                        .paymentMethod("Ví Của Tôi")
                        .orderCode(orderCode)
                        .wallet(adminWallet)
                        .amount(order.getAmount())
                        .build();
                transactionRepo.save(transaction);
                // neu don hang co trang thai thanh cong thi
                //tự động gọi hàm sheduled để check và chuyển tiền vào ví người bán
            }
        } else {
            Wallet walletBuyer = walletRepo.findById(user.getId()).get();
            if (walletBuyer.getAmount().compareTo(post.getInitPrice()) < 0) {
                throw new ResponseStatusException(HttpStatus.valueOf(200), "VÍ CỦA BẠN KHÔNG ĐỦ TIỀN VUI LONG NẠP TIỀN VÀ THỬ LẠI");
            } else {
                Order order = Order.builder()
                        .user(user)
                        .shipAddress(addOrderRequestDTO.getShipAddress())
                        .orderDate(LocalDate.now())
                        .post(post)
                        .amount(post.getInitPrice())
                        .note(addOrderRequestDTO.getNote())
                        .paymentMethod("VÍ CỦA TÔI")
                        .deliveryMethod("Khách Hàng Tự Thỏa Thuận")
                        .status("WaitingForConfirmation")
                        .paymentStatus("DEPOSITED")
                        .codeOrder(orderCode)
                        .build();
                order = orderRepo.save(order);
                orderResponseDTO = OrderResponseDTO.builder()
                        .orderId(order.getId())
                        .postId(order.getPost().getId())
                        .userId(order.getUser().getId())
                        .shipAddress(order.getShipAddress())
                        .orderDate(order.getOrderDate())
                        .amount(order.getAmount())
                        .note(order.getNote())
                        .paymentMethod(order.getPaymentMethod())
                        .deliveryMethod(order.getDeliveryMethod())
                        .status(order.getStatus())
                        .form(post.getForm())
                        .paymentStatus(order.getPaymentStatus())
                        .build();
                // tru tien trong vi va luu lai
                walletBuyer.setAmount(walletBuyer.getAmount().subtract(order.getAmount()));
                walletRepo.save(walletBuyer);
                transaction = Transaction.builder()
                        .createAt(LocalDate.now())
                        .type("Đặt Cọc")
                        .paymentMethod("Ví Của Tôi")
                        .orderCode(orderCode)
                        .wallet(walletBuyer)
                        .amount(order.getAmount())
                        .build();
                transactionRepo.save(transaction);
                // cong tien vao vi admin
                User admin = userRepo.findUserByRole_Id(1L);
                Wallet adminWallet = walletRepo.findById(admin.getId()).get();
                adminWallet.setAmount(adminWallet.getAmount().add(order.getAmount()));
                walletRepo.save(adminWallet);
                transaction = Transaction.builder()
                        .createAt(LocalDate.now())
                        .type("Admin Nhận Đăt Cọc")
                        .paymentMethod("Ví Của Tôi")
                        .orderCode(orderCode)
                        .wallet(adminWallet)
                        .amount(order.getAmount())
                        .build();
                transactionRepo.save(transaction);
                // neu don hang đến tay người nhận và sau khi người nhận xác nhận ngày gửi lại và người bán xác nhận đã nhận được hàng
                //tự động gọi hàm sheduled để check và chuyển tiền vào ví người mua đồng thời trừ tiền hoa hồng
            }
        }
        return orderResponseDTO;
    }

    @Override
    public Boolean converOrderStatus(Long orderId) {
        Order order = orderRepo.findById(orderId).get();
        try {
            if (order.getStatus().equalsIgnoreCase("WaitingForConfirmation")) {
                order.setStatus("processing");
                orderRepo.save(order);
                return true;
            }
            if (order.getStatus().equalsIgnoreCase("processing")) {
                order.setStatus("delivery");
                orderRepo.save(order);
                return true;
            }
            if (order.getStatus().equalsIgnoreCase("delivery")) {
                order.setStatus("complete");
                orderRepo.save(order);
                return true;
            }
            if (order.getStatus().equalsIgnoreCase("resent")) {
                order.setStatus("received");
                orderRepo.save(order);
                return true;
            }
            if (order.getStatus().equalsIgnoreCase("complete")) {
                order.setStatus("resent");
                orderRepo.save(order);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<OrderResponseDTO> getALLOrder() {
        List<Order> orderList = null;
        List<OrderResponseDTO> responseDTOS = new ArrayList<>();
        try {
            orderList = orderRepo.findAll();
            for (Order order : orderList) {
                Post post = postRepo.findById(order.getId()).get();
                List<Book> books = bookRepo.findAllByPost_Id(post.getId());
                List<BookPendingResponseDTO> listBookResponse = new ArrayList<>();
                for (Book book: books) {
                    BookPendingResponseDTO dto = BookPendingResponseDTO.builder()
                            .imageBook(book.getImageList())
                            .reprints(book.getReprints())
                            .isbn(book.getIsbn())
                            .language(book.getLanguage())
                            .author(book.getBookAuthor().getName())
                            .publicCompany(book.getPublicCompany())
                            .name(book.getName())
                            .bookExchange(book.getName())
                            .statusQuo(book.getStatusQuo())
                            .description(book.getDescription())
                            .coverType(book.getCoverType())
                            .publicationDate(book.getPublicationDate())
                            .build();
                    listBookResponse.add(dto);
                }
                OrderResponseDTO orderResponseDTO = OrderResponseDTO.builder()
                        .orderId(order.getId())
                        .postId(order.getPost().getId())
                        .shipAddress(order.getShipAddress())
                        .orderDate(order.getOrderDate())
                        .amount(order.getAmount())
                        .note(order.getNote())
                        .paymentMethod(order.getPaymentMethod())
                        .orderCode(order.getCodeOrder())
                        .deliveryMethod(order.getDeliveryMethod())
                        .userId(order.getUser().getId())
                        .status(order.getStatus())
                        .postImage(post.getImageUrl())
                        .listBooks(listBookResponse)
                        .paymentStatus(order.getPaymentStatus())
                        .build();
                responseDTOS.add(orderResponseDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseDTOS;
    }

    @Override
    public Order cancelOrder(Long orderId, String cancelReason) {
        Order order = orderRepo.findById(orderId).get();
        if (order.getStatus().equalsIgnoreCase("processing")) {
            User userReceivered = userRepo.findById(order.getUser().getId()).get();
            Post post = postRepo.findById(order.getPost().getId()).get();
            post.setPostStatus("active");
            postRepo.save(post);
            order.setStatus("cancel");
            order.setCancelReason(cancelReason);
            orderRepo.save(order);
            // lưu vào trong bản transaction

        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(200), "Huy Đơn Không Thành Công");
        }
        return order;
    }

    @Override
    public Boolean updateResentDate(Long orderId, String resentDate) {
        Order order = orderRepo.findById(orderId).get();
        try {
            if (order.getStatus().equalsIgnoreCase("complete")) {
                order.setResentDate(LocalDate.parse(resentDate));
                order.setStatus("resent");
                orderRepo.save(order);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<OrderHistoryResponseDTO> getAllSellOrder(Long userId, String status) {
        List<Order> orderList = new ArrayList<>();
        List<OrderHistoryResponseDTO> orderHistoryResponseDTOS = new ArrayList<>();
        try {
            orderList = orderRepo.findAll();
            for (Order order : orderList) {
                if (order.getPost().getUser().getId().equals(userId)) {
                    if (status.equalsIgnoreCase("null")) {
                        OrderHistoryResponseDTO history = OrderHistoryResponseDTO.builder()
                                .order_date(order.getOrderDate())
                                .cancelReason(order.getCancelReason())
                                .resentDate(order.getResentDate())
                                .postImage(order.getPost().getImageUrl())
                                .title(order.getPost().getTitle())
                                .amount(order.getAmount())
                                .deliveryMethod(order.getDeliveryMethod())
                                .status(order.getStatus())
                                .paymentMethod(order.getPaymentMethod())
                                .receiverName(order.getUser().getName())
                                .orderCode(order.getCodeOrder())
                                .phoneNumber(order.getUser().getPhoneNumber())
                                .shipAddress(order.getShipAddress())
                                .paymentStatus(order.getPaymentStatus())
                                .orderId(order.getId())
                                .build();
                        orderHistoryResponseDTOS.add(history);
                    } else {
                        if (order.getStatus().equalsIgnoreCase(status)) {
                            OrderHistoryResponseDTO history = OrderHistoryResponseDTO.builder()
                                    .order_date(order.getOrderDate())
                                    .cancelReason(order.getCancelReason())
                                    .resentDate(order.getResentDate())
                                    .postImage(order.getPost().getImageUrl())
                                    .title(order.getPost().getTitle())
                                    .amount(order.getAmount())
                                    .deliveryMethod(order.getDeliveryMethod())
                                    .status(order.getStatus())
                                    .orderCode(order.getCodeOrder())
                                    .paymentMethod(order.getPaymentMethod())
                                    .receiverName(order.getUser().getName())
                                    .phoneNumber(order.getUser().getPhoneNumber())
                                    .shipAddress(order.getShipAddress())
                                    .paymentStatus(order.getPaymentStatus())
                                    .orderId(order.getId())
                                    .build();
                            orderHistoryResponseDTOS.add(history);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderHistoryResponseDTOS;
    }

    @Override
    public List<OrderHistoryResponseDTO> getAllBoughtOrder(Long userId, String status) {
        List<Order> orderList = new ArrayList<>();
        List<OrderHistoryResponseDTO> orderHistoryResponseDTOS = new ArrayList<>();
        try {
            orderList = orderRepo.findAll();
            for (Order order : orderList) {
                if (order.getUser().getId().equals(userId)) {
                    if (status.equalsIgnoreCase("null")) {
                        OrderHistoryResponseDTO history = OrderHistoryResponseDTO.builder()
                                .order_date(order.getOrderDate())
                                .cancelReason(order.getCancelReason())
                                .resentDate(order.getResentDate())
                                .postImage(order.getPost().getImageUrl())
                                .title(order.getPost().getTitle())
                                .amount(order.getAmount())
                                .deliveryMethod(order.getDeliveryMethod())
                                .status(order.getStatus())
                                .paymentMethod(order.getPaymentMethod())
                                .orderCode(order.getCodeOrder())
                                .shipAddress(order.getShipAddress())
                                .paymentStatus(order.getPaymentStatus())
                                .orderId(order.getId())
                                .form(order.getPost().getForm())
                                .sellerName(order.getPost().getUser().getName())
                                .build();
                        orderHistoryResponseDTOS.add(history);
                    } else {
                        if (order.getStatus().equalsIgnoreCase(status)) {
                            OrderHistoryResponseDTO history = OrderHistoryResponseDTO.builder()
                                    .order_date(order.getOrderDate())
                                    .cancelReason(order.getCancelReason())
                                    .resentDate(order.getResentDate())
                                    .postImage(order.getPost().getImageUrl())
                                    .orderCode(order.getCodeOrder())
                                    .title(order.getPost().getTitle())
                                    .amount(order.getAmount())
                                    .deliveryMethod(order.getDeliveryMethod())
                                    .status(order.getStatus())
                                    .paymentMethod(order.getPaymentMethod())
                                    .shipAddress(order.getShipAddress())
                                    .paymentStatus(order.getPaymentStatus())
                                    .form(order.getPost().getForm())
                                    .orderId(order.getId())
                                    .sellerName(order.getPost().getUser().getName())
                                    .build();
                            orderHistoryResponseDTOS.add(history);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderHistoryResponseDTOS;
    }

    @Override
    public List<OrderHistoryResponseDTO> getAllOrderByStatus(Long userId, String status) {
        List<Order> orderList = null;
        List<OrderHistoryResponseDTO> orderHistoryResponseDTOS = new ArrayList<>();
        try {
            orderList = orderRepo.findAll();
            for (Order order : orderList) {
                if (order.getPost().getUser().getId().equals(userId) && order.getStatus().equalsIgnoreCase(status)) {
                    OrderHistoryResponseDTO history = OrderHistoryResponseDTO.builder()
                            .order_date(order.getOrderDate())
                            .cancelReason(order.getCancelReason())
                            .resentDate(order.getResentDate())
                            .postImage(order.getPost().getImageUrl())
                            .title(order.getPost().getTitle())
                            .amount(order.getAmount())
                            .deliveryMethod(order.getDeliveryMethod())
                            .status(order.getStatus())
                            .paymentMethod(order.getPaymentMethod())
                            .orderCode(order.getCodeOrder())
                            .shipAddress(order.getShipAddress())
                            .paymentStatus(order.getPaymentStatus())
                            .orderId(order.getId())
                            .build();
                    orderHistoryResponseDTOS.add(history);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderHistoryResponseDTOS;
    }

    @Override
    public List<RevenueResponseDTO> profitCalculation(String month, String year) {
        Map<Integer, BigDecimal> integerBigDecimalMap = new HashMap<>();
        List<RevenueResponseDTO> revenueResponseDTOS = new ArrayList<>();
        List<Order> orderList = null;
        try {
            if (!month.equalsIgnoreCase("null") && !year.equalsIgnoreCase("null")) {
                LocalDate date = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1);
                Integer integer = date.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
                for (int i = 1; i <= integer; i++) {
                    integerBigDecimalMap.put(i, BigDecimal.valueOf(0));
                }
                orderList = orderRepo.findAllByOrderDate(Integer.parseInt(year), Integer.parseInt(month));
                for (Order order : orderList) {
                    if (order.getPost().getForm().equalsIgnoreCase("Bán")) {
                        if (integerBigDecimalMap.containsKey(order.getOrderDate().getDayOfMonth())) {
                            integerBigDecimalMap.put(order.getOrderDate().getDayOfMonth(), integerBigDecimalMap.get(order.getOrderDate().getDayOfMonth()).add(order.getAmount().subtract(order.getAmount().multiply(BigDecimal.valueOf(0.8)))));
                        }
                    } else {
                        if (integerBigDecimalMap.containsKey(order.getOrderDate().getDayOfMonth())) {
                            integerBigDecimalMap.put(order.getOrderDate().getDayOfMonth(), integerBigDecimalMap.get(order.getOrderDate().getDayOfMonth()).add(order.getAmount().subtract(order.getAmount().multiply(BigDecimal.valueOf(0.9)))));
                        }
                    }
                }
                Set set = integerBigDecimalMap.keySet();
                for (Object key : set) {
                    RevenueResponseDTO revenueResponseDTO = RevenueResponseDTO.builder()
                            .day(Integer.parseInt(key + ""))
                            .amount(integerBigDecimalMap.get(key))
                            .build();
                    revenueResponseDTOS.add(revenueResponseDTO);
                }
            }
            if (month.equalsIgnoreCase("null") && !year.equalsIgnoreCase("null")) {
                for (int i = 1; i <= 12; i++) {
                    integerBigDecimalMap.put(i, BigDecimal.valueOf(0));
                }
                orderList = orderRepo.findAllByYear(Integer.parseInt(year));
                for (Order order : orderList) {
                    if (order.getPost().getForm().equalsIgnoreCase("Bán")) {
                        if (integerBigDecimalMap.containsKey(order.getOrderDate().getMonthValue())) {
                            integerBigDecimalMap.put(order.getOrderDate().getMonthValue(), integerBigDecimalMap.get(order.getOrderDate().getMonthValue()).add(order.getAmount().subtract(order.getAmount().multiply(BigDecimal.valueOf(0.8)))));
                        }
                    } else {
                        if (integerBigDecimalMap.containsKey(order.getOrderDate().getMonthValue())) {
                            integerBigDecimalMap.put(order.getOrderDate().getMonthValue(), integerBigDecimalMap.get(order.getOrderDate().getMonthValue()).add(order.getAmount().subtract(order.getAmount().multiply(BigDecimal.valueOf(0.9)))));
                        }
                    }
                }
                Set set = integerBigDecimalMap.keySet();
                for (Object key : set) {
                    RevenueResponseDTO revenueResponseDTO = RevenueResponseDTO.builder()
                            .month(Integer.parseInt(key + ""))
                            .amount(integerBigDecimalMap.get(key))
                            .build();
                    revenueResponseDTOS.add(revenueResponseDTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return revenueResponseDTOS;
    }
}
