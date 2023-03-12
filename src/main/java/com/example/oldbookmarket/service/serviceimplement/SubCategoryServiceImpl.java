package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.entity.Subcategory;
import com.example.oldbookmarket.repository.SubcategoryRepo;
import com.example.oldbookmarket.service.serviceinterface.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SubCategoryServiceImpl implements SubCategoryService {
    @Autowired
    SubcategoryRepo subcategoryRepo;

    @Override
    public List<Subcategory> getSubCategoryByCategoryId(Long id) {
        List<Subcategory> subcategoryList = null;
        try {
            subcategoryList = subcategoryRepo.findAllByCategory_Id(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return subcategoryList;
    }

}
