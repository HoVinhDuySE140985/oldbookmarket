package com.example.oldbookmarket.dto.request.userDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChangePasswordRequestDTO {
    private String email;
    private String newPassword;
    private String oldPassword;
    private String confirmPassword;
}
