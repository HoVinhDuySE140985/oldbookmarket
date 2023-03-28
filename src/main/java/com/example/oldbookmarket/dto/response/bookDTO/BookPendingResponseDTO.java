package com.example.oldbookmarket.dto.response.bookDTO;

import com.example.oldbookmarket.entity.BookImage;
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
public class BookPendingResponseDTO {
    private Long bookId;
    private String name;
    private String isbn;
    private List<BookImage> imageBook;
    private LocalDate publicationDate;
    private String publicCompany;
    private String author;
    private String coverType;
    private String language;
    private String bookExchange;
    private String statusQuo;
    private String description;
}
