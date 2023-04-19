package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.entity.PostNotification;
import com.example.oldbookmarket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostNotificationRepo extends JpaRepository<PostNotification, Long> {
    List<PostNotification> findAllByBookNoty(String bookNoty);
}
