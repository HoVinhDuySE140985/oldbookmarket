package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.response.categoryDTO.CategoryResponseDTO;
import com.example.oldbookmarket.dto.response.categoryDTO.TopCategoryResponeDTO;
import com.example.oldbookmarket.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategory();
    List<CategoryResponseDTO> getAllCateAndSubCate();
    Category createNewCategory(String cateName);
    List<TopCategoryResponeDTO> getTopCatePublication();
}
