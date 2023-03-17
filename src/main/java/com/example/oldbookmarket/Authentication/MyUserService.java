package com.example.oldbookmarket.Authentication;

import com.example.oldbookmarket.entity.User;
import com.example.oldbookmarket.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserService implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findUserByEmail(username);
        if (user == null){
            throw new UsernameNotFoundException("USER_NOT_FOUND");
        }
        return new UserDetail(user);
    }
}
