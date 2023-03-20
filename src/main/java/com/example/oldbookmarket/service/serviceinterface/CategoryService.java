package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.respone.CategoryResponseDTO;
import com.example.oldbookmarket.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategory();
    List<CategoryResponseDTO> getAllCateAndSubCate();
    Category createNewCategory(String cateName);
}
