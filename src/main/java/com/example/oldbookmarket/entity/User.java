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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String gender;
    private String imageUrl;
    private LocalDate  dob;
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Address> addressList;

//    @JsonIgnore
//    @OneToMany(mappedBy = "user")
//    private List<User_Role> userRoles;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<UserStatus> userStatuses;

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
    private List<Refund> refundList;
}
