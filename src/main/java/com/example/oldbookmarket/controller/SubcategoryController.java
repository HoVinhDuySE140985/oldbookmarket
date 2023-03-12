package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.respone.ResponeDTO;
import com.example.oldbookmarket.entity.Subcategory;
import com.example.oldbookmarket.enumcode.ErrorCode;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponeDTO> getSubCategoryByCateId(@PathVariable Long id){
        ResponeDTO responeDTO = new ResponeDTO();
        try {
            List<Subcategory> subcategoryList = subCategoryService.getSubCategoryByCategoryId(id);
            if( subcategoryList != null){
                responeDTO.setData(subcategoryList);
                responeDTO.setSuccessCode(SuccessCode.Get_All_Success);
            }else {
                responeDTO.setErrorCode(ErrorCode.NOT_FOUND);
            }
        }catch (Exception e){
            e.printStackTrace();
            responeDTO.setErrorCode(ErrorCode.Get_All_Fail);
        }
        return  ResponseEntity.ok().body(responeDTO);
    }
}
