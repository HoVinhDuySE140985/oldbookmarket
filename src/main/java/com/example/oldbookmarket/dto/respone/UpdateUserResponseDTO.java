package com.example.oldbookmarket.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserResponseDTO {
//    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private String gender;
    private String imageUrl;
    private LocalDate dob;
    private String password;
}
