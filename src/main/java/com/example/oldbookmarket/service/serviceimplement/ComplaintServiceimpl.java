package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.ComplaintDTO.ComplaintRequestDTO;
import com.example.oldbookmarket.dto.request.NotiRequestDTO.PnsRequest;
import com.example.oldbookmarket.dto.response.complaintDTO.ComplaintResponseDTO;
import com.example.oldbookmarket.entity.Complaint;
import com.example.oldbookmarket.entity.Order;
import com.example.oldbookmarket.entity.User;
import com.example.oldbookmarket.repository.ComplaintRepo;
import com.example.oldbookmarket.repository.OrderRepo;
import com.example.oldbookmarket.repository.UserRepo;
import com.example.oldbookmarket.service.serviceinterface.ComplaintService;
import com.example.oldbookmarket.service.serviceinterface.FcmService;
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
    FcmService fcmService;

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
                    .status(1)
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
                Order order = orderRepo.findByCodeOrder(complaint.getOrder().getCodeOrder());
                complaintResponseDTO = ComplaintResponseDTO.builder()
                        .id(complaint.getId())
                        .title(complaint.getTitle())
                        .complaintImage(complaint.getComplaintImage())
                        .createAt(complaint.getCreateAt())
                        .orderCode(complaint.getOrder().getCodeOrder())
                        .description(complaint.getDescription())
                        .userComplained(complaint.getUserComplained())
                        .userComplainedId(order.getPost().getUser().getId())
                        .senderId(complaint.getUser().getId())
                        .build();
                complaintResponseDTOS.add(complaintResponseDTO);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return complaintResponseDTOS;
    }

    @Override
    public Boolean rejectComplaint(Long senderId, Long complaintId) {
        try {
            Complaint complaint = complaintRepo.findById(complaintId).get();
            complaint.setStatus(0);
            complaintRepo.save(complaint);

            List<String> fcmKey = new ArrayList<>();
            User sender = userRepo.findById(senderId).get();
            if (!sender.getFcmKey().isEmpty() && sender.getFcmKey() != null) {
                fcmKey.add(sender.getFcmKey());
            }
            if (!fcmKey.isEmpty() || fcmKey.size() > 0) { // co key
                // pushnoti
                PnsRequest pnsRequest = new PnsRequest(fcmKey, "Từ Chối",
                        "Khiếu nại của bạn bị từ chối");
                fcmService.pushNotification(pnsRequest);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
