package com.example.oldbookmarket.dto.request.userDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ForgotPasswordRequestDTO {
    private String email;
    private String randomCode;
    private String newPassword;
}
