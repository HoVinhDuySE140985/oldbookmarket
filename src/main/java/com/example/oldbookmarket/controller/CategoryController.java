package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.response.categoryDTO.CategoryResponseDTO;
import com.example.oldbookmarket.dto.response.ResponseDTO;
import com.example.oldbookmarket.dto.response.categoryDTO.TopCategoryResponeDTO;
import com.example.oldbookmarket.entity.Category;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("get-all-category")
    @PermitAll
    public ResponseEntity<ResponseDTO> getAllCategory(){
        ResponseDTO responseDTO = new ResponseDTO();
            List<Category> categoryList = categoryService.getAllCategory();
            try {
                responseDTO.setData(categoryList);
                responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
            }catch (Exception e){
                e.printStackTrace();
            }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-all-category-and-subcategory")
    @PermitAll
    public ResponseEntity<ResponseDTO> getAllCateAndSubCate(){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<CategoryResponseDTO> categoryResponseDTO = categoryService.getAllCateAndSubCate();
            responseDTO.setData(categoryResponseDTO);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("staff/create-category/{cateName}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ResponseDTO> createNewCategory(@PathVariable @Validated String cateName){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Category category = categoryService.createNewCategory(cateName);
            responseDTO.setData(category);
            responseDTO.setSuccessCode(SuccessCode.CREATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get_top_category_publication")
    @PermitAll
    public ResponseEntity<ResponseDTO> getTopCategoryPublication(){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<TopCategoryResponeDTO> topCategoryList = categoryService.getTopCatePublication();
            responseDTO.setData(topCategoryList);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  ResponseEntity.ok().body(responseDTO);
    }
}
