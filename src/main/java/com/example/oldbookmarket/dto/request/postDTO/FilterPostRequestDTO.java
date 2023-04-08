package com.example.oldbookmarket.dto.request.postDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FilterPostRequestDTO {
    private String sortBy;
    private String city;
}
