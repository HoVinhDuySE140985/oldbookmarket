package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.response.ResponseDTO;
import com.example.oldbookmarket.dto.response.subcategoryResponseDTO.SubcategoryResponseDTO;
import com.example.oldbookmarket.entity.Subcategory;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
@RequestMapping("/api/subcategory")
public class SubcategoryController {
    @Autowired
    SubCategoryService subCategoryService;

    @GetMapping("get-all-subcategory/{id}")
    @PermitAll
    public ResponseEntity<ResponseDTO> getSubCategoryByCateId(@PathVariable @Validated Long id){
        ResponseDTO responseDTO = new ResponseDTO();
            List<Subcategory> subcategoryList = subCategoryService.getSubCategoryByCategoryId(id);
            try {
                responseDTO.setData(subcategoryList);
                responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
            }catch (Exception e){
                e.printStackTrace();
            }
        return  ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("staff/create-subcategory/{cateId}/{subCateName}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ResponseDTO> createNewSubCategory(@PathVariable @Validated Long cateId,
                                                            @PathVariable @Validated String subCateName){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Subcategory subcategory = subCategoryService.createNewSubcategory(cateId, subCateName);
            responseDTO.setData(subcategory);
            responseDTO.setSuccessCode(SuccessCode.CREATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-publication-subcategory")
    @PermitAll
    public ResponseEntity<ResponseDTO> getPublicationSubcategory(){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<SubcategoryResponseDTO> subcategoryResponseDTOList = subCategoryService.getPublicationSubcategories();
            responseDTO.setData(subcategoryResponseDTOList);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }
}
