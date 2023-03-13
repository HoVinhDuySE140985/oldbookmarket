package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post, Long> {
}
