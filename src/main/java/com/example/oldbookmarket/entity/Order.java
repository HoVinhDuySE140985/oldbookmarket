package com.example.oldbookmarket.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "[order]")
public class Order implements Serializable {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    @Column(length = 50)
    private String shipAddress;
    private LocalDate orderDate;
    private BigDecimal amount;
    private String note;
    @Column(length = 20)
    private String paymentMethod;
    @Column(length = 20)
    private String deliveryMethod;
    @Column(length = 20)
    private String status;
    @Column(length = 20)
    private String paymentStatus;
    private LocalDate resentDate;
    private String cancelReason;
    @Column(length = 15)
    private String codeOrder;

    @MapsId
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<Complaint> complaintList;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<Transaction> transactions;
}
