package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.orderDTO.AddOrderRequestDTO;
import com.example.oldbookmarket.dto.respone.OrderResponseDTO;
import com.example.oldbookmarket.entity.Address;
import com.example.oldbookmarket.entity.Order;
import com.example.oldbookmarket.entity.Post;
import com.example.oldbookmarket.repository.AddressRepo;
import com.example.oldbookmarket.repository.OrderRepo;
import com.example.oldbookmarket.repository.PostRepo;
import com.example.oldbookmarket.service.serviceinterface.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    PostRepo postRepo;

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    OrderRepo orderRepo;

    @Override
    public OrderResponseDTO createNewOrder(AddOrderRequestDTO addOrderRequestDTO) {
        OrderResponseDTO orderResponseDTO = null;
        Post post = new Post();
        try {
            post = postRepo.findById(addOrderRequestDTO.getPostId()).get();
            post.setPostStatus("deactivate");
            postRepo.save(post);
            Address address = addressRepo.findAddressByIdAndUserId(addOrderRequestDTO.getAddressId(), addOrderRequestDTO.getUserId());
            String shipadrress = address.getCity() + "," + address.getProvince() + "," + address.getDistrict() + "," + address.getWard() + "," + address.getStreet();
            Order order = Order.builder()
//                    .id(post.getId())
                    .shipAddress(shipadrress)
                    .post(post)
                    .orderDate(addOrderRequestDTO.getOrderDate())
                    .amount(addOrderRequestDTO.getAmount())
                    .note(addOrderRequestDTO.getNote())
                    .paymentMethod("MOMO")
                    .deliveryMethod("Khách Hàng Tự Thỏa Thuận")
                    .status("processing")
                    .build();
            orderRepo.save(order);

            orderResponseDTO = OrderResponseDTO.builder()
//                    .orderId(order.getId())
                    .postId(order.getPost().getId())
                    .shipAddress(order.getShipAddress())
                    .orderDate(order.getOrderDate())
                    .amount(order.getAmount())
                    .note(order.getNote())
                    .paymentMethod(order.getPaymentMethod())
                    .deliveryMethod(order.getDeliveryMethod())
                    .status(order.getStatus())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderResponseDTO;
    }

    @Override
    public Order converOrderStatus(Long orderId) {
        Order order = orderRepo.getById(orderId);
        try {
            if (order.getStatus().equalsIgnoreCase("processing")) {
                order.setStatus("packed");
            }
            if (order.getStatus().equalsIgnoreCase("packed")) {
                order.setStatus("delivery");
            }
            if (order.getStatus().equalsIgnoreCase("delivery")){
                order.setStatus("receive");
            }
            orderRepo.save(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    @Override
    public List<Order> getAllOrder(Long userId, String orderStatus) {
        List<Order> orderList = new ArrayList<>();
        try {
            orderList = orderRepo.getAllByUser_IdAndStatus(userId,orderStatus);
        }catch (Exception e){
            e.printStackTrace();
        }
        return orderList;
    }

    @Override
    public Order cancelOrder(Long orderId) {
        Order order = new Order();
        try {
            order = orderRepo.getById(orderId);
            Post post = postRepo.getById(order.getPost().getId());
            post.setPostStatus("active");
            postRepo.save(post);
            order.setStatus("cancel");
            orderRepo.save(order);
        }catch (Exception e){
            e.printStackTrace();
        }
        return order;
    }
}
