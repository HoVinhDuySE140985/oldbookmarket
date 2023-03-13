package com.example.oldbookmarket.dto.respone;

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
public class PostResponeDTO {
    private Long id;
    private String title;
    private String imageUrl;
    private String location;
    private String status;
    private List<Book> bookList;
}
