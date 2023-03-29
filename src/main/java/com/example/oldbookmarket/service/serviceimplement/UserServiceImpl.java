package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.userDTO.RegisterRequestDTO;
import com.example.oldbookmarket.dto.request.userDTO.UpdateUserRequestDTO;
import com.example.oldbookmarket.dto.response.userDTO.RegisterResponseDTO;
import com.example.oldbookmarket.dto.response.userDTO.TopUserResponseDTO;
import com.example.oldbookmarket.dto.response.userDTO.UpdateUserResponseDTO;
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

import java.util.ArrayList;
import java.util.List;

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
            user.setPhoneNumber(registerRequestDTO.getPhoneNumber());
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
                    .phoneNumber(user.getPhoneNumber())
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
            User user = userRepo.getById(updateUserRequestDTO.getId());
            if (user != null){
                user.setName(updateUserRequestDTO.getName());
//                user.setEmail(updateUserRequestDTO.getEmail());
                user.setImageUrl(updateUserRequestDTO.getImageUrl());
                user.setPhoneNumber(updateUserRequestDTO.getPhoneNumber());
                user.setDob(updateUserRequestDTO.getDob());
                user.setGender(updateUserRequestDTO.getGender());
//                user.setPassword(encoder.encode(updateUserRequestDTO.getPassword()));
                user = userRepo.save(user);

                updateUserResponseDTO = UpdateUserResponseDTO.builder()
                        .name(user.getName())
//                        .email(user.getEmail())
                        .imageUrl(user.getImageUrl())
                        .phoneNumber(user.getPhoneNumber())
                        .dob(user.getDob())
                        .gender(user.getGender())
//                        .password(user.getPassword())
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
            user = userRepo.findById(id).get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<TopUserResponseDTO> getAllUsersHasHighestOrder() {
        List<TopUserResponseDTO> topUserResponseDTOS = new ArrayList<>();
        try {
            topUserResponseDTOS = userRepo.findUsersHasHighestOrder();
        }catch (Exception e){
            e.printStackTrace();
        }
        return topUserResponseDTOS;
    }
}
