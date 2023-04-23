package com.example.oldbookmarket.dto.response.bookDTO;

import com.example.oldbookmarket.entity.BookImage;
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
public class BooKDetailResponseDTO {
    private Long bookId;
    private String name;
    private String isbn;
    private List<BookImage> bookImages;
    private LocalDate publicationDate;
    private String publicCompany;
    private String author;
    private String coverType;
    private String reprint;
    private String language;
    private String bookExchange;
    private String statusQuo;
    private String description;
    private String subcategoryName;
    private Long userId;
    private String userName;
    private String phoneNumber;
    private BigDecimal price;
    private BigDecimal initPrice;
    private LocalDate createAt;
}
