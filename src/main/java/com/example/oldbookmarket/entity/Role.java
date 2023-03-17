package com.example.oldbookmarket.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

//    @JsonIgnore
//    @OneToMany(mappedBy = "role")
//    private List<User_Role> user_roles;
    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private List<User> users;
}
