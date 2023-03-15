package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.entity.Category;
import com.example.oldbookmarket.entity.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubcategoryRepo extends JpaRepository<Subcategory, Long> {
    List<Subcategory> findAllByCategory_Id(Long id);

    Subcategory findByName(String name);

}
