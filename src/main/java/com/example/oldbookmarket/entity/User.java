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
    private Integer phoneNumber;
    private String gender;
    private String imageUrl;
    private LocalDate  dob;
    private String password;
    private String userToken;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Address> addressList;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<User_Role> userRoles;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<User_Status> userStatuses;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Post> postList;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Report> reportList;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Complaint> complaints;

}
