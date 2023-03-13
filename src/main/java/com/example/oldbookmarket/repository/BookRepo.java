package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book, Long> {
}
