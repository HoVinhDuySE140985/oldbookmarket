package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.response.bookauthorDTO.BookAuthorResponseDTO;
import com.example.oldbookmarket.entity.BookAuthor;
import com.example.oldbookmarket.repository.BookAuthorRepo;
import com.example.oldbookmarket.service.serviceinterface.BookAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookAuthorServiceImpl implements BookAuthorService {

    @Autowired
    BookAuthorRepo bookAuthorRepo;
    @Override
    public List<BookAuthorResponseDTO> getAllAuthor(String keyWord) {
        List<BookAuthorResponseDTO> responseDTOS = new ArrayList<>();
        try {
            List<BookAuthor> bookAuthors = bookAuthorRepo.findAllByKeyWord(keyWord);
            for (BookAuthor bookAuthor: bookAuthors) {
                BookAuthorResponseDTO responseDTO = BookAuthorResponseDTO.builder()
                        .authorId(bookAuthor.getId())
                        .author(bookAuthor.getName())
                        .build();
                responseDTOS.add(responseDTO);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
         return responseDTOS;
    }
}
