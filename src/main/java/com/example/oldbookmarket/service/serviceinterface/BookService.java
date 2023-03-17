package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.request.BookRequestDTO.UpdateBookResquestDTO;
import com.example.oldbookmarket.dto.respone.BookImageResponseDTO;
import com.example.oldbookmarket.dto.respone.BookResponseDTO;

public interface BookService {
    BookResponseDTO getBookInfor(Long postId, Long bookId);
    BookResponseDTO updateBookInfo(UpdateBookResquestDTO updateBookResquestDTO);

    BookImageResponseDTO getAllImageOfBook(Long postId);
}
