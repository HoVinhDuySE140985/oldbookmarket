package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.dto.response.userDTO.TopUserResponseDTO;
import com.example.oldbookmarket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    
    User findUserByEmail(String email);

    User findUserByName(String name);

    @Query("SELECT new com.example.oldbookmarket.dto.response.userDTO.TopUserResponseDTO(u.id, count(o.post.id)) \n" +
            "from User u, Post p, Order o \n" +
            "where u.id = p.user.id and p.id = o.post.id and o.status = 'completed' \n" +
            "group by u.id \n" +
            "order by  count(o.post.id) desc")
    List<TopUserResponseDTO> findUsersHasHighestOrder();

}
