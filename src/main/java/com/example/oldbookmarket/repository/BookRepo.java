package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepo extends JpaRepository<Book, Long> {
        List<Book> findAllByPost_Id(Long id);
}
