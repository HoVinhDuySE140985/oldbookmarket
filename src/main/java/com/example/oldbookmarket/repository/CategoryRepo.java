package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {
}
