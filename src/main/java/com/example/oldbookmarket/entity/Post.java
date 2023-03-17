package com.example.oldbookmarket.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String title;
    private String form;
    private String imageUrl;
    private LocalDate createAt;
    private String postStatus;
    private Double initPrice;
    private Double price;
    private String bookExchange;
    private String reasonReject;
    private String location;
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "post")
    private List<Book> books;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToOne(mappedBy = "post")
    private Order order;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;
}
