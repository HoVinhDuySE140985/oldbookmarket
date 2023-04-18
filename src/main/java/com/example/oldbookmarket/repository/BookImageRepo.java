package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.dto.response.bookDTO.BookImageResponseDTO;
import com.example.oldbookmarket.entity.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookImageRepo extends JpaRepository<BookImage, Long> {
//    List<String> findAllByBook_Id(Long id);

    List<BookImage> findAllByBook_Id(long bookId);
}
