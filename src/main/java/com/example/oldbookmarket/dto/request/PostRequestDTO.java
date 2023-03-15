package com.example.oldbookmarket.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private LocalDate createAt;
    private String form;
    private List<BookSellRequestDTO> bookSellList;
    private List<BookExchangeRequestDTO> bookExchangeList;
    private Long userId;
    private Long categoryId;
    private Long subCategoryId;
    private String reasonReject;
}
