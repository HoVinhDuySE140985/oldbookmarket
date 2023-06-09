package com.example.oldbookmarket.dto.request.postDTO;

import com.example.oldbookmarket.dto.request.bookDTO.BookRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PostRequestDTO {
    private Long id;
    private String title;
    private String imageUrl;
    private String form;
    private List<BookRequestDTO> bookList;
    private BigDecimal initPrice;
    private BigDecimal price;
    private String bookExchange;
    private Long userId;
    private Long categoryId;
    private Long subCategoryId;
    private String reasonReject;
    private String location;
}
