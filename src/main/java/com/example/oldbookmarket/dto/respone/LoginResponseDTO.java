package com.example.oldbookmarket.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LoginResponseDTO {
    private Long id;
    private String name;
    private String imageUrl;
    private String email;
    private Integer phoneNumber;
    private String gender;
    private LocalDate dob;
    private Long  role;
    private String password;
    private String token;




}
