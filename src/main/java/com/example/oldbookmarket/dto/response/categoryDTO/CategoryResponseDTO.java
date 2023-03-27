package com.example.oldbookmarket.dto.response.categoryDTO;

import com.example.oldbookmarket.entity.Subcategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private List<Subcategory> subcategoryList;
}
