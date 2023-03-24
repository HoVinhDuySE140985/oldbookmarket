package com.example.oldbookmarket.dto.request.orderDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FinalOrderRequestDTO {
    private Long orderId;
    private Long userId;
}
