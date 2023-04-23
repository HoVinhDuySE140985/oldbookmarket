package com.example.oldbookmarket.dto.response.complaintDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintResponseDTO {
    private Long id;
    private LocalDate createAt;
    private String title;
    private String complaintImage;
    private String description;
    private String userComplained;
    private Long userComplainedId;
    private String orderCode;
    private Long senderId;
}
