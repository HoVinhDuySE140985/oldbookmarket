package com.example.oldbookmarket.dto.response.postDTO;

import com.example.oldbookmarket.dto.response.bookDTO.BookResponseDTO;
import com.example.oldbookmarket.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PostPendingResponseDTO {
    private Long id;
    private String title;
    private String form;
    private String imageUrl;
    private String location;
    private String status;
    private String description;
    private List<Book> bookList;
    private BigDecimal initPrice;
    private BigDecimal price;
    private Long userId;
    private String userName;
    private String reasonReject;
}
