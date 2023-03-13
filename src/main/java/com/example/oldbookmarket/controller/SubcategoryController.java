package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.respone.ResponeDTO;
import com.example.oldbookmarket.entity.Subcategory;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
@RequestMapping("/api/subcategory")
public class SubcategoryController {
    @Autowired
    SubCategoryService subCategoryService;

    @GetMapping("get-all-subcategory/{id}")
    @PermitAll
    public ResponseEntity<ResponeDTO> getSubCategoryByCateId(@PathVariable Long id){
        ResponeDTO responeDTO = new ResponeDTO();
            List<Subcategory> subcategoryList = subCategoryService.getSubCategoryByCategoryId(id);
            if( subcategoryList != null){
                responeDTO.setData(subcategoryList);
                responeDTO.setSuccessCode(SuccessCode.Get_All_Success);
            }else {
                throw new ResponseStatusException(HttpStatus.valueOf(404),"NOT_FOUND");
            }
        return  ResponseEntity.ok().body(responeDTO);
    }
}
