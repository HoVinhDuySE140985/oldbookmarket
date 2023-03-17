package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.respone.ResponseDTO;
import com.example.oldbookmarket.entity.Category;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
            if (categoryList != null){
                responseDTO.setData(categoryList);
                responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
            }else {
                throw new ResponseStatusException(HttpStatus.valueOf(404),"NOT_FOUND");
            }
        return ResponseEntity.ok().body(responseDTO);
    }
}
