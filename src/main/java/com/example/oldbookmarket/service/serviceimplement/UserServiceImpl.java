package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.UserRequestDTO.RegisterRequestDTO;
import com.example.oldbookmarket.dto.request.UserRequestDTO.UpdateUserRequestDTO;
import com.example.oldbookmarket.dto.respone.RegisterResponseDTO;
import com.example.oldbookmarket.dto.respone.UpdateUserResponseDTO;
import com.example.oldbookmarket.entity.User;
import com.example.oldbookmarket.entity.UserStatus;
import com.example.oldbookmarket.enumcode.StatusCode;
import com.example.oldbookmarket.repository.RoleRepo;
import com.example.oldbookmarket.repository.UserRepo;
import com.example.oldbookmarket.repository.UserStatusRepo;
import com.example.oldbookmarket.service.serviceinterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserStatusRepo userStatusRepo;

    @Autowired
    RoleRepo roleRepo;
    @Override
    public User findByEmail(String email) {
        User user = new User();
        try {
            user = userRepo.findUserByEmail(email);
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public RegisterResponseDTO createUser(RegisterRequestDTO registerRequestDTO) {
        RegisterResponseDTO registerResponseDTO = null;
        try {
            User user = new User();
            user.setName(registerRequestDTO.getName());
            user.setEmail(registerRequestDTO.getEmail());
            user.setPassword(encoder.encode(registerRequestDTO.getPassword()));
            user.setRole(roleRepo.findByName("CUSTOMER"));
            user = userRepo.save(user);

            UserStatus userStatus = UserStatus.builder()
                    .user(user)
                    .name(StatusCode.ACTIVATE.toString())
                    .build();
            userStatusRepo.save(userStatus);

            registerResponseDTO = RegisterResponseDTO.builder()
                    .userId(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return registerResponseDTO;
    }

    @Override
    public UpdateUserResponseDTO updateUserInfo(UpdateUserRequestDTO updateUserRequestDTO) {
        UpdateUserResponseDTO updateUserResponseDTO = null;
        try {
            User result = userRepo.getById(updateUserRequestDTO.getId());
            if (result != null){
                User user = new User();
                user.setName(updateUserRequestDTO.getName());
                user.setEmail(updateUserRequestDTO.getEmail());
                user.setImageUrl(updateUserRequestDTO.getImageUrl());
                user.setPhoneNumber(updateUserRequestDTO.getPhoneNumber());
                user.setDob(updateUserRequestDTO.getDob());
                user.setGender(updateUserRequestDTO.getGender());
                user.setPassword(encoder.encode(updateUserRequestDTO.getPassword()));
                user = userRepo.save(user);

                updateUserResponseDTO = UpdateUserResponseDTO.builder()
                        .name(user.getName())
                        .email(user.getEmail())
                        .imageUrl(user.getImageUrl())
                        .phoneNumber(user.getPhoneNumber())
                        .dob(user.getDob())
                        .gender(user.getGender())
                        .password(user.getPassword())
                        .build();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return updateUserResponseDTO;
    }

    @Override
    public User findUserById(Long id) {
        User user = new User();
        try {
            user = userRepo.getById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }
}
