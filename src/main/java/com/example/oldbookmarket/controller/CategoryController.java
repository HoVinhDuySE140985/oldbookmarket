package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.respone.ResponeDTO;
import com.example.oldbookmarket.entity.Category;
import com.example.oldbookmarket.enumcode.ErrorCode;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("get-all-category")
    @PermitAll
    public ResponseEntity<ResponeDTO> getAllCategory(){
        ResponeDTO responeDTO = new ResponeDTO();
        try {
            List<Category> categoryList = categoryService.getAllCategory();
            if (categoryList != null){
                responeDTO.setData(categoryList);
                responeDTO.setSuccessCode(SuccessCode.Get_All_Success);
            } else {
                responeDTO.setErrorCode(ErrorCode.NOT_FOUND);
            }
        }catch (Exception e){
            e.printStackTrace();
            responeDTO.setErrorCode(ErrorCode.Get_All_Fail);
        }
        return ResponseEntity.ok().body(responeDTO);
    }
}
