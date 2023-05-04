package com.example.oldbookmarket.dto.response.orderDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DataResponseDTO {
    private int numberUser;
    private int numberOrder;
}
