package com.example.oldbookmarket.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PostResponseDTO {
    private Long id;
    private String title;
    private String form;
    private String imageUrl;
    private String location;
    private String status;
    private String description;
//    private List<Book> bookList;
//    private Double initPrice;
    private BigDecimal price;
//    private String categoryExchange;
    private Long userId;
    private String userName;
//    private Long subCategoryId;
//    private Long categoryId;
//    private String reasonReject;
}
