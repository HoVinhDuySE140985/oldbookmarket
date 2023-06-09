package com.example.oldbookmarket.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postNotyId;
    @Column(length = 40)
    private String email;
    @Column(length = 100)
    private String bookNoty;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}
