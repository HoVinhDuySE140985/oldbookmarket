package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.respone.CategoryResponseDTO;
import com.example.oldbookmarket.entity.Category;
import com.example.oldbookmarket.entity.Subcategory;
import com.example.oldbookmarket.repository.CategoryRepo;
import com.example.oldbookmarket.repository.SubcategoryRepo;
import com.example.oldbookmarket.service.serviceinterface.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    SubcategoryRepo subcategoryRepo;

    @Override
    public List<Category> getAllCategory() {
        List<Category> categoryList = null;
        try {
            categoryList = categoryRepo.findAll();
        }catch (Exception e){
            e.printStackTrace();
        }
        return categoryList;
    }

    @Override
    public List<CategoryResponseDTO> getAllCateAndSubCate() {
        List<Category> categoryList = null;
        List<CategoryResponseDTO> categoryResponseDTOList = new ArrayList<>();
        try {
            categoryList = categoryRepo.findAll();
            for (Category category: categoryList) {
                CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
                categoryResponseDTO.setId(category.getId());
                categoryResponseDTO.setName(category.getName());
                categoryResponseDTO.setSubcategoryList(category.getSubcategoryList());
                categoryResponseDTOList.add(categoryResponseDTO);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return categoryResponseDTOList;
    }

    @Override
    public Category createNewCategory(String cateName) {
        Category category = new Category();
        try {
            category.setName(cateName);
            categoryRepo.save(category);
        }catch (Exception e){
            e.printStackTrace();
        }
        return category;
    }
}
