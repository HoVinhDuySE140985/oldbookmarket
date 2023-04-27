package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.request.userDTO.ChangePasswordRequestDTO;
import com.example.oldbookmarket.dto.request.userDTO.ForgotPasswordRequestDTO;
import com.example.oldbookmarket.dto.request.userDTO.RegisterRequestDTO;
import com.example.oldbookmarket.dto.request.userDTO.UpdateUserRequestDTO;
import com.example.oldbookmarket.dto.response.userDTO.*;
import com.example.oldbookmarket.entity.User;

import java.util.List;

public interface UserService {

    UserLoginResponseDTO findByEmail(String email);

    RegisterResponseDTO createUser(RegisterRequestDTO registerRequestDTO);

    UpdateUserResponseDTO updateUserInfo(UpdateUserRequestDTO updateUserRequestDTO);

    User findUserByEmail(String email);

    User save(User user);

    Boolean isExistUserByEmail(String email);

    List<TopUserResponseDTO> getAllUsersHasHighestOrder();

    ChangePasswordReponseDTO changePassWord(ChangePasswordRequestDTO changePasswordRequestDTO);

    List<UserResponseDTO> getAllUser(String email);

    Boolean banUser(String email);

    String sendVerificationCode(String email);
    User forgotPassword(ForgotPasswordRequestDTO forgotPasswordRequestDTO);

    User findUserByName(String name);
//    User searchEmailByKeyWord(String email);
}
