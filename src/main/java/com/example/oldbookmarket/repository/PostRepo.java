package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.dto.response.addressDTO.CityResponseDTO;
import com.example.oldbookmarket.dto.response.postDTO.PostResponseDTO;
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

    @Query("select p  " +
            "from Post p  left join Book b on p.id = b.post.id left join BookAuthor ba on ba.id = b.bookAuthor.id " +
            "where (p.title LIKE %:keyWord%) or (ba.name LIKE %:keyWord%)")
    List<Post> findByKeyWord(String keyWord, Sort sort);

    @Query("select distinct p\n" +
            "from Post p  left join Book b on p.id = b.post.id left join BookAuthor ba on ba.id = b.bookAuthor.id  \n" +
            "where ((p.title LIKE %:keyWord%) or (ba.name LIKE %:keyWord%)) and p.postStatus = 'active'")
    List<Post> findByKeyWord(String keyWord);

    List<Post> findAllBySubcategory_Id(Long subcategoryId);
    List<Post> findAllBySubcategory_Id(Long subcategoryId,Sort sort);

    List<Post> findTop10ByPostStatusOrderByCreateAtDesc(String Status);

    List<Post> findAllByUser_Id(Long userId);

    @Query("Select count(p.id)\n" +
            "from Post as p \n" +
            "where p.user.id = :userId ")
    Integer findAllPost(Long userId);
}
