package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.request.userDTO.RegisterRequestDTO;
import com.example.oldbookmarket.dto.request.userDTO.UpdateUserRequestDTO;
import com.example.oldbookmarket.dto.response.userDTO.RegisterResponseDTO;
import com.example.oldbookmarket.dto.response.userDTO.UpdateUserResponseDTO;
import com.example.oldbookmarket.entity.User;

public interface UserService {

    User findByEmail(String email);

    RegisterResponseDTO createUser(RegisterRequestDTO registerRequestDTO);

    UpdateUserResponseDTO updateUserInfo(UpdateUserRequestDTO updateUserRequestDTO);

    User findUserById(Long id);
}
