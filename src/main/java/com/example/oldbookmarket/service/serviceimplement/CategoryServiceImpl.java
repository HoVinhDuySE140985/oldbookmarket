package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.entity.Category;
import com.example.oldbookmarket.repository.CategoryRepo;
import com.example.oldbookmarket.service.serviceinterface.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepo categoryRepo;

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
}
