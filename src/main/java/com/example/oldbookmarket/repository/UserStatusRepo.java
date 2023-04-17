package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepo extends JpaRepository<UserStatus, Long> {
    UserStatus findByUserId(Long id);

}
