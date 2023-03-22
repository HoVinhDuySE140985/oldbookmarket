package com.example.oldbookmarket.dto.request.MomoDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MomoRequestDTO {
    private Long orderId;
    private String returnURL;
    private String notifyURL;
}
