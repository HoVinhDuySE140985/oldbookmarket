package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.dto.response.categoryDTO.TopCategoryResponeDTO;
import com.example.oldbookmarket.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    Category findByName(String name);
    @Query("Select  new com.example.oldbookmarket.dto.response.categoryDTO.TopCategoryResponeDTO( c.id, count(p.id)) \n" +
            "From Category c , Subcategory s, Post  p \n" +
            "where c.id=s.category.id and s.id=p.subcategory.id and p.postStatus = 'active'\n" +
            "group by c.id \n" +
            "order by count(p.id) desc ")
    List<TopCategoryResponeDTO> findTopCategoryByPost();
}
