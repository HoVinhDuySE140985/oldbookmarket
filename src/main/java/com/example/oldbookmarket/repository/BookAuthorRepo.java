package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.entity.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookAuthorRepo extends JpaRepository<BookAuthor, Long> {
    BookAuthor findByName(String authorName);

    @Query("select ba from BookAuthor ba where ba.name LIKE %:keyWord% ")
    List<BookAuthor> findAllByKeyWord(String keyWord);
}
