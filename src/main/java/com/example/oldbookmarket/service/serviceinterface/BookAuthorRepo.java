package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.entity.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookAuthorRepo extends JpaRepository<BookAuthor, Long> {
    BookAuthor findByName(String authorName);
}
