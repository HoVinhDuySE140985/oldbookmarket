package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.entity.Category;
import com.example.oldbookmarket.entity.Subcategory;
import com.example.oldbookmarket.repository.CategoryRepo;
import com.example.oldbookmarket.repository.SubcategoryRepo;
import com.example.oldbookmarket.service.serviceinterface.CategoryService;
import com.example.oldbookmarket.service.serviceinterface.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SubCategoryServiceImpl implements SubCategoryService {
    @Autowired
    SubcategoryRepo subcategoryRepo;

    @Autowired
    CategoryRepo categoryRepo;

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

    @Override
    public Subcategory createNewSubcategory(Long cateId, String subcateName) {
        Subcategory subcategory = new Subcategory();
        try {
            Category category = categoryRepo.getById(cateId);
            subcategory = Subcategory.builder()
                    .category(category)
                    .name(subcateName)
                    .build();
            subcategoryRepo.save(subcategory);
        }catch (Exception e){
            e.printStackTrace();
        }
        return subcategory;
    }
}
