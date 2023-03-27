package com.example.oldbookmarket.dto.response.categoryDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TopCategoryResponeDTO {
    private Long categoryId;
    private Long quantity;
}
