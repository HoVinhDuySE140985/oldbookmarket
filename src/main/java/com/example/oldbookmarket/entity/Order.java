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
@Table(name = "[order]")
public class Order {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    private String shipAddress;
    private LocalDate orderDate;
    private Double amount;
    private String note;
    private String paymentMethod;
    private String deliveryMethod;
    private String status;

    @MapsId
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<Complaint> complaintList;


    @JsonIgnore
    @OneToOne(mappedBy = "order")
    private Refund refund;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
}
