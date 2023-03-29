package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.request.userDTO.ChangePasswordRequestDTO;
import com.example.oldbookmarket.dto.request.userDTO.RegisterRequestDTO;
import com.example.oldbookmarket.dto.request.userDTO.UpdateUserRequestDTO;
import com.example.oldbookmarket.dto.response.userDTO.ChangePasswordReponseDTO;
import com.example.oldbookmarket.dto.response.userDTO.RegisterResponseDTO;
import com.example.oldbookmarket.dto.response.userDTO.TopUserResponseDTO;
import com.example.oldbookmarket.dto.response.userDTO.UpdateUserResponseDTO;
import com.example.oldbookmarket.entity.User;

import java.util.List;

public interface UserService {

    User findByEmail(String email);

    RegisterResponseDTO createUser(RegisterRequestDTO registerRequestDTO);

    UpdateUserResponseDTO updateUserInfo(UpdateUserRequestDTO updateUserRequestDTO);

    User findUserById(Long id);
    List<TopUserResponseDTO> getAllUsersHasHighestOrder();

    ChangePasswordReponseDTO changePassWord(ChangePasswordRequestDTO changePasswordRequestDTO);
}
