package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.request.bookDTO.UpdateBookResquestDTO;
import com.example.oldbookmarket.dto.response.bookDTO.BookImageResponseDTO;
import com.example.oldbookmarket.dto.response.bookDTO.BookResponseDTO;
import com.example.oldbookmarket.dto.response.ResponseDTO;
import com.example.oldbookmarket.dto.response.bookauthorDTO.BookAuthorResponseDTO;
import com.example.oldbookmarket.entity.BookAuthor;
import com.example.oldbookmarket.entity.PostNotification;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.BookAuthorService;
import com.example.oldbookmarket.service.serviceinterface.BookService;
import com.example.oldbookmarket.service.serviceinterface.PostNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookController {
    @Autowired
    BookService bookService;

    @Autowired
    BookAuthorService bookAuthorService;

    @Autowired
    PostNotificationService postNotificationService;

    @GetMapping("get-book-by/{postId}")
    @PermitAll
    public ResponseEntity<ResponseDTO> getBookInfor(@PathVariable @Validated Long postId){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<BookResponseDTO> booklist = bookService.getBookInfor(postId);
            responseDTO.setData(booklist);
            responseDTO.setSuccessCode(SuccessCode.Get_Success);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-all-image-book-by/{postId}")
    @PermitAll
        public ResponseEntity<ResponseDTO> getAllBookImage(@PathVariable @Validated Long postId){
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
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> updateBookInformation( @Validated UpdateBookResquestDTO updateBookResquestDTO){
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

    @GetMapping("get-all-author-by-Key-word")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> getAllAuthorBykeyWord(@RequestParam String keyWord){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<BookAuthorResponseDTO> authorList = bookAuthorService.getAllAuthor(keyWord);
            responseDTO.setData(authorList);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("create-book-to-get-noti")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> createBookToGetNoti(@RequestParam Long userId,
                                                           @RequestParam String bookReceive ){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setData(postNotificationService.createBookToGetNoty(userId,bookReceive));
            responseDTO.setSuccessCode(SuccessCode.CREATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  ResponseEntity.ok().body(responseDTO);
    }

}
