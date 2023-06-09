package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.request.bookDTO.UpdateBookResquestDTO;
import com.example.oldbookmarket.dto.response.bookDTO.BooKDetailResponseDTO;
import com.example.oldbookmarket.dto.response.bookDTO.BookImageResponseDTO;
import com.example.oldbookmarket.dto.response.bookDTO.BookResponseDTO;

import java.util.List;

public interface BookService {
    List<BooKDetailResponseDTO> getBookInfo(Long postId);
    BookResponseDTO updateBookInfo(UpdateBookResquestDTO updateBookResquestDTO);

//    BookImageResponseDTO getAllImageOfBook(Long postId);

    BookResponseDTO getBookById(Long bookId);
}
