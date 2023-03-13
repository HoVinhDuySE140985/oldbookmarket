package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

}
