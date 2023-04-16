package com.example.oldbookmarket.dto.response.subcategoryResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubcategoryResponseDTO {
    private String subName;
    private String subcateImage;
    private Long amount;
}
