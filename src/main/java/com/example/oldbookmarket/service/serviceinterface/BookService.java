package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.request.bookDTO.UpdateBookResquestDTO;
import com.example.oldbookmarket.dto.respone.BookImageResponseDTO;
import com.example.oldbookmarket.dto.respone.BookResponseDTO;

import java.util.List;

public interface BookService {
    List<BookResponseDTO> getBookInfor(Long postId);
    BookResponseDTO updateBookInfo(UpdateBookResquestDTO updateBookResquestDTO);

    BookImageResponseDTO getAllImageOfBook(Long postId);
}
