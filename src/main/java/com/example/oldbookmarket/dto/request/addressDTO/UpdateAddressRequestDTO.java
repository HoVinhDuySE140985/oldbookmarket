package com.example.oldbookmarket.dto.request.addressDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UpdateAddressRequestDTO {
    private Long userId;
    private Long addressId;
    private String city;
    private String province;
    private String district;
    private String ward;
    private String street;
//    private String Status;
}
