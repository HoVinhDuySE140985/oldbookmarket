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
    private LocalDate orderDate;
    private Double amount;
    private String note;

    @MapsId
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<Complaint> complaintList;

    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<Order_Status> order_statusList;

    @JsonIgnore
    @OneToOne(mappedBy = "")
    private Refund refund;
}
