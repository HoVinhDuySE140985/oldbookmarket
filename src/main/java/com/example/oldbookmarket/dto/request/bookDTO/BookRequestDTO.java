package com.example.oldbookmarket.dto.request.bookDTO;


import com.example.oldbookmarket.dto.response.bookauthorDTO.BookAuthorResponseDTO;
import com.example.oldbookmarket.entity.BookAuthor;
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
public class BookRequestDTO {
    private String name;
    private String isbn;
    private List<String> bookImages;
    private LocalDate publicationDate;
    private String reprints;
    private String publicCompany;
    private List<String> author;
    private String coverType;
    private String language;
    private String statusQuo;
    private String description;
}
