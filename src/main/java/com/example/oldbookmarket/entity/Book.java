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
    @Column(length = 70,nullable = false)
    private String name;
    @Column(length = 40)
    private String isbn;
    private LocalDate publicationDate;
    @Column(length = 20)
    private String reprints; // lần tái bản
    @Column(length = 30)
    private String publicCompany;
    @Column(length = 15)
    private String coverType;
    @Column(length = 15)
    private String language;
    private String statusQuo; // đồng nghĩa với condition
    @Column(length = 500)
    private String description;



    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @JsonIgnore
    @OneToMany(mappedBy = "book")
    private List<BookImage> imageList;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authorId")
    private BookAuthor bookAuthor;
}
