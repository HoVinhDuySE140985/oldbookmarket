package com.example.oldbookmarket.dto.response.userDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String gender;
    private String imageUrl;
    private LocalDate dob;
    private String password;
    private String status;
//    private String fcmKey;
}
