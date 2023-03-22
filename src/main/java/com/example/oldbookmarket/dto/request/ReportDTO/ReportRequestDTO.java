package com.example.oldbookmarket.dto.request.ReportDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReportRequestDTO {
    private Long userId;
}
