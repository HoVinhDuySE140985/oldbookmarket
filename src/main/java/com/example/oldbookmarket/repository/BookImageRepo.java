package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.entity.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookImageRepo extends JpaRepository<BookImage, Long> {
}
