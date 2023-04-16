package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.response.subcategoryResponseDTO.SubcategoryResponseDTO;
import com.example.oldbookmarket.entity.Subcategory;

import java.util.List;

public interface SubCategoryService {
    List<Subcategory> getSubCategoryByCategoryId(Long id);
    Subcategory createNewSubcategory(Long cateId, String subcateName);

    List<SubcategoryResponseDTO> getPublicationSubcategories();
}
