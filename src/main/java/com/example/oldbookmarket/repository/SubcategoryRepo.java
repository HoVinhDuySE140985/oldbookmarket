package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.dto.response.subcategoryResponseDTO.SubcategoryResponseDTO;
import com.example.oldbookmarket.entity.Category;
import com.example.oldbookmarket.entity.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubcategoryRepo extends JpaRepository<Subcategory, Long> {
    List<Subcategory> findAllByCategory_Id(Long id);

    @Query("select distinct new com.example.oldbookmarket.dto.response.subcategoryResponseDTO.SubcategoryResponseDTO(sc.name, sc.subcateImage, count(p.id))\n" +
            "from Subcategory as sc join Post as p on sc.id = p.subcategory.id \n" +
            "where p.postStatus = 'active'\n" +
            "group by sc.id\n" +
            "order by count(p.id) desc")
    List<SubcategoryResponseDTO> findAllPublicationSubcategories();

}
