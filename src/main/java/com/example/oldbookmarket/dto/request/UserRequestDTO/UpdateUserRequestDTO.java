package com.example.oldbookmarket.dto.request.UserRequestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UpdateUserRequestDTO {
    private Long id;
    private String name;
    private Integer phoneNumber;
    private String email;
    private String gender;
    private String imageUrl;
    private LocalDate dob;
    private String password;
}
