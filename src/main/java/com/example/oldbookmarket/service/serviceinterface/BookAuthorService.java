package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.response.bookauthorDTO.BookAuthorResponseDTO;

import java.util.List;

public interface BookAuthorService {
    List<BookAuthorResponseDTO> getAllAuthor(String keyWord);
}
