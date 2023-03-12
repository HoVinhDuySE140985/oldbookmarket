package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategory();
}
