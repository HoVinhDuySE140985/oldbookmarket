package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.entity.PostNotification;
import com.example.oldbookmarket.entity.User;
import com.example.oldbookmarket.repository.PostNotificationRepo;
import com.example.oldbookmarket.repository.UserRepo;
import com.example.oldbookmarket.service.serviceinterface.PostNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostNotificationServiceImpl implements PostNotificationService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    PostNotificationRepo postNotificationRepo;

    @Override
    public Boolean createBookToGetNoty(Long userId, String bookReceive) {
        User user = userRepo.findById(userId).get();
        try {
            PostNotification postNotification = PostNotification.builder()
                    .user(user)
                    .email(user.getEmail())
                    .bookNoty(bookReceive)
                    .build();
            postNotificationRepo.save(postNotification);

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
