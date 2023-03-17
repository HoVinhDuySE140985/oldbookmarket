package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.request.BookRequestDTO.UpdateBookResquestDTO;
import com.example.oldbookmarket.dto.respone.BookImageResponseDTO;
import com.example.oldbookmarket.dto.respone.BookResponseDTO;
import com.example.oldbookmarket.dto.respone.ResponseDTO;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book")
public class BookController {
    @Autowired
    BookService bookService;

    @GetMapping("get-book-by-image/{postId}/{bookId}")
    public ResponseEntity<ResponseDTO> getBookInfor(@PathVariable Long postId, Long bookId){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            BookResponseDTO bookResponseDTO = bookService.getBookInfor(postId,bookId);
            responseDTO.setData(bookResponseDTO);
            responseDTO.setSuccessCode(SuccessCode.Get_Success);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-all-imagebook/{postId}")
        public ResponseEntity<ResponseDTO> getAllBookImage(@PathVariable Long postId){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            BookImageResponseDTO bookImageResponseDTO = bookService.getAllImageOfBook(postId);
            responseDTO.setData(bookImageResponseDTO);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("update-book-information/{postId}/{bookId}")
    public ResponseEntity<ResponseDTO> updateBookInformation(UpdateBookResquestDTO updateBookResquestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            BookResponseDTO bookResponseDTO = bookService.updateBookInfo(updateBookResquestDTO);
            responseDTO.setData(bookResponseDTO);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

}
