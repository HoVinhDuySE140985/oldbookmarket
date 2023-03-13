package com.example.oldbookmarket.dto.request;

import com.example.oldbookmarket.entity.Book;
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
public class PostRequestDTO {
    private String title;
    private String imageUrl;
    private LocalDate createAt;
    private String status;
    private List<BookRequestDTO> bookList;
    private Long userId;
    private Long subCategoryId;
}
