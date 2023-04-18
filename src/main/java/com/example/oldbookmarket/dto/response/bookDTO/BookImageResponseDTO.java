package com.example.oldbookmarket.dto.response.bookDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BookImageResponseDTO {
    private String imageUrl;
    private Long imageId;
}
