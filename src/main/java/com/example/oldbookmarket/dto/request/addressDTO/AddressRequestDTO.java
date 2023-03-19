package com.example.oldbookmarket.dto.request.addressDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AddressRequestDTO {
    private Long userId;
    private String city;
    private String province;
    private String district;
    private String ward;
    private String street;
}
