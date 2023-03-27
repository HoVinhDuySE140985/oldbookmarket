package com.example.oldbookmarket.dto.response.userDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TopUserResponseDTO {
    private Long userId;
    private Long count;
}
