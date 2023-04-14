package com.example.oldbookmarket.dto.response.bookauthorDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookAuthorResponseDTO {
    private Long authorId;
    private String author;
}
