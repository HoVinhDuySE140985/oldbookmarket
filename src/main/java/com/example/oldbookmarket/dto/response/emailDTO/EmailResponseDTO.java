package com.example.oldbookmarket.dto.response.emailDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailResponseDTO {
    private String email;
    private String massage;
    private String subject;
}
