package com.example.oldbookmarket.dto.request.ComplaintDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintRequestDTO {
    private String title;
    private String complaintImage;
    private String description;
    private String orderCode;
    private Long senderId;
}
