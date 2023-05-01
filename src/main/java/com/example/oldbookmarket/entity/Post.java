package com.example.oldbookmarket.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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
    @Column(length = 200)
    private String title;
    @Column(length = 10)
    private String form;
    private String imageUrl;
    private LocalDate createAt;
    @Column(length = 20)
    private String postStatus;
    private BigDecimal initPrice;
    private BigDecimal price;
    @Column(length = 100)
    private String bookExchange;
    private String reasonReject;
    private String location;
    private LocalDate expDate;
    private int isCheck;

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
