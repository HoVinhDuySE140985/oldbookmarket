package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.ComplaintDTO.ComplaintRequestDTO;
import com.example.oldbookmarket.dto.response.complaintDTO.ComplaintResponseDTO;
import com.example.oldbookmarket.entity.Complaint;
import com.example.oldbookmarket.entity.Order;
import com.example.oldbookmarket.entity.User;
import com.example.oldbookmarket.repository.ComplaintRepo;
import com.example.oldbookmarket.repository.OrderRepo;
import com.example.oldbookmarket.repository.UserRepo;
import com.example.oldbookmarket.service.serviceinterface.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComplaintServiceimpl implements ComplaintService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    ComplaintRepo complaintRepo;
    @Override
    public ComplaintResponseDTO createComplaint(ComplaintRequestDTO complaintRequestDTO) {
        ComplaintResponseDTO complaintResponseDTO = null;
        try {
            User sender = userRepo.findById(complaintRequestDTO.getSenderId()).get();
            Order order = orderRepo.findByCodeOrder(complaintRequestDTO.getOrderCode());
            Complaint complaint = Complaint.builder()
                    .createAt(LocalDate.now())
                    .title(complaintRequestDTO.getTitle())
                    .complaintImage(complaintRequestDTO.getComplaintImage())
                    .description(complaintRequestDTO.getDescription())
                    .userComplained(order.getPost().getUser().getName())
                    .order(order)
                    .user(sender)
                    .build();
            complaint = complaintRepo.save(complaint);
            complaintResponseDTO = ComplaintResponseDTO.builder()
                    .id(complaint.getId())
                    .createAt(complaint.getCreateAt())
                    .title(complaint.getTitle())
                    .complaintImage(complaint.getComplaintImage())
                    .description(complaint.getDescription())
                    .userComplained(complaint.getUserComplained())
                    .senderId(complaint.getUser().getId())
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return complaintResponseDTO;
    }

    @Override
    public List<ComplaintResponseDTO> getAllComplaint() {
        List<Complaint> complaints = null;
        List<ComplaintResponseDTO> complaintResponseDTOS = new ArrayList<>();
        ComplaintResponseDTO complaintResponseDTO = null;
        try {
            complaints = complaintRepo.findAll();
            for (Complaint complaint: complaints) {
                complaintResponseDTO = ComplaintResponseDTO.builder()
                        .id(complaint.getId())
                        .title(complaint.getTitle())
                        .complaintImage(complaint.getComplaintImage())
                        .createAt(complaint.getCreateAt())
                        .orderCode(complaint.getOrder().getCodeOrder())
                        .description(complaint.getDescription())
                        .userComplained(complaint.getUserComplained())
                        .senderId(complaint.getUser().getId())
                        .build();
                complaintResponseDTOS.add(complaintResponseDTO);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return complaintResponseDTOS;
    }
}
