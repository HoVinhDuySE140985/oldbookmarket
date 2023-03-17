package com.example.oldbookmarket.entity;




import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String isbn;
    private LocalDate publicationDate;
    private String publicCompany;
    private String author;
    private String coverType;
    private String language;
    private String statusQuo; // đồng nghĩa với condition
//    private String description;



    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @JsonIgnore
    @OneToMany(mappedBy = "book")
    private List<BookImage> imageList;
}
