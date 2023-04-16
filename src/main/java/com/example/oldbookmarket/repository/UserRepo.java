package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.dto.response.userDTO.TopUserResponseDTO;
import com.example.oldbookmarket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    
    User findUserByEmail(String email);

    User findUserByName(String name);

    @Query("Select distinct  new com.example.oldbookmarket.dto.response.userDTO.TopUserResponseDTO(u.id, u.name, u.imageUrl, count(p.id))\n" +
            "from User as u join Post as p on u.id = p.user.id\n" +
            "               join Order as o on o.post.id = p.id\n" +
            "where o.status like 'complete'\n" +
            "group by u.id \n" +
            "order by count(p.id) desc")
    List<TopUserResponseDTO> findUsersHasHighestOrder();

    User findUserByRole_Id(Long id);

}
