package com.example.oldbookmarket.dto.response.reportDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReportResponseDTO {
    private Long id;
    private Long approvedBy;
    private String orderCode;
    private String reason;
    private String emailReported;
}
