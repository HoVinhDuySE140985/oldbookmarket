package com.example.oldbookmarket.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 40, nullable = false)
    private String name;
    @Column(length = 30, nullable = false,unique = true)
    private String email;
    @Column(length = 10)
    private String phoneNumber;
    @Column(length = 5)
    private String gender;
    private String imageUrl;
    private LocalDate  dob;
    @Column (nullable = false)
    private String password;
    @Column(length = 10)
    private String userStatus;
    private String verificationCode;
    private String fcmKey;
    

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Address> addressList;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Post> postList;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Report> reportList;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Complaint> complaints;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Order> orderList;

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private Wallet wallet;


    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<PostNotification> postNotifications;
}
