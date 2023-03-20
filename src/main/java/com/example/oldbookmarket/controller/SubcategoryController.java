package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.respone.ResponseDTO;
import com.example.oldbookmarket.entity.Subcategory;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<ResponseDTO> getSubCategoryByCateId(@PathVariable Long id){
        ResponseDTO responseDTO = new ResponseDTO();
            List<Subcategory> subcategoryList = subCategoryService.getSubCategoryByCategoryId(id);
            if( subcategoryList != null){
                responseDTO.setData(subcategoryList);
                responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
            }else {
                throw new ResponseStatusException(HttpStatus.valueOf(404),"NOT_FOUND");
            }
        return  ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("staff/create-subcategory/{cateId}/{subCateName}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ResponseDTO> createNewSubCategory(@PathVariable Long cateId,
                                                            @PathVariable String subCateName){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Subcategory subcategory = subCategoryService.createNewSubcategory(cateId, subCateName);
            responseDTO.setData(subcategory);
            responseDTO.setSuccessCode(SuccessCode.CREATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.valueOf("400"),"CREATE_FAIL");
        }
        return ResponseEntity.ok().body(responseDTO);
    }
}
