package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.request.UserRequestDTO.RegisterRequestDTO;
import com.example.oldbookmarket.dto.request.UserRequestDTO.UpdateUserRequestDTO;
import com.example.oldbookmarket.dto.respone.RegisterResponseDTO;
import com.example.oldbookmarket.dto.respone.UpdateUserResponseDTO;
import com.example.oldbookmarket.entity.User;

public interface UserService {

    User findByEmail(String email);

    RegisterResponseDTO createUser(RegisterRequestDTO registerRequestDTO);

    UpdateUserResponseDTO updateUserInfo(UpdateUserRequestDTO updateUserRequestDTO);

    User findUserById(Long id);
}
