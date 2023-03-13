package com.example.oldbookmarket.dto.request;

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
public class BookRequestDTO {
    private String name;
    private String isbn;
    private List<String> bookImages;
    private LocalDate publicationDate;
    private String publicCompany;
    private String author;
    private String coverType;
    private String language;
    private String statusQuo;
    private String description;
    private Double initPrice;
    private Double price;

}
