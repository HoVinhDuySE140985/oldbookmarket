package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.entity.Category;
import com.example.oldbookmarket.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Long> {
   List<Post> getAllByUser_Id(Long id);
    List<Post> findAllByTitle(String title);
    @Query("select p from Post p where p.title LIKE %:keyWord%")
    List<Post> findByKeyWord(String keyWord);
    Category findBySubcategory_Id(Long id);
}
