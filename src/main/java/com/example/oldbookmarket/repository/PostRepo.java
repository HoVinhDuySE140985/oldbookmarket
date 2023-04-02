package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.dto.response.addressDTO.CityResponseDTO;
import com.example.oldbookmarket.entity.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepo extends JpaRepository<Post, Long> {
    @Query("select DISTINCT p.location from Post p")
    List<String> findAllCity();

    List<Post> findAll(Sort sort);

    @Query("select p from Post p where p.title LIKE %:keyWord%")
    List<Post> findByKeyWord(String keyWord, Sort sort);

    @Query("select p from Post p where p.title LIKE %:keyWord%")
    List<Post> findByKeyWord(String keyWord);

    List<Post> findAllBySubcategory_Id(Long subcategoryId);

}
