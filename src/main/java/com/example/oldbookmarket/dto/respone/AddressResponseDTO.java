package com.example.oldbookmarket.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddressResponseDTO {
    private Long id;
    private String city;
    private String province;
    private String district;
    private String ward;
    private String street;
//    private String Status;
}
