package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.Jwt.JwtConfig;
import com.example.oldbookmarket.dto.request.userDTO.ChangePasswordRequestDTO;
import com.example.oldbookmarket.dto.request.userDTO.LoginRequestDTO;
import com.example.oldbookmarket.dto.request.userDTO.RegisterRequestDTO;
import com.example.oldbookmarket.dto.request.userDTO.UpdateUserRequestDTO;
import com.example.oldbookmarket.dto.response.userDTO.*;
import com.example.oldbookmarket.dto.response.ResponseDTO;
import com.example.oldbookmarket.entity.User;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.UserService;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthenController {
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JwtConfig jwtConfig;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenController(AuthenticationManager authenticationManager, UserService userService, JwtConfig jwtConfig, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtConfig = jwtConfig;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("login")
    @PermitAll
    public ResponseEntity<ResponseDTO> login(@RequestBody @Validated LoginRequestDTO loginRequestDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());

        try {
            Authentication authenticate = authenticationManager.authenticate(authentication);
            System.out.println(authenticate.getName());

            if (authenticate.isAuthenticated()) {
                User userAuthenticated = userService.findByEmail(authenticate.getName());
                String token = Jwts.builder().setSubject(authenticate.getName())
                        .claim(("authorities"), authenticate.getAuthorities())
                        .claim("id", userAuthenticated.getId())
                        .setIssuedAt((new Date())).setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                        .signWith(jwtConfig.secretKey()).compact();

                LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                        .id(userAuthenticated.getId())
                        .name(userAuthenticated.getName())
                        .email(userAuthenticated.getEmail())
                        .phoneNumber(userAuthenticated.getPhoneNumber())
                        .gender(userAuthenticated.getGender())
                        .imageUrl(userAuthenticated.getImageUrl())
                        .dob(userAuthenticated.getDob())
                        .password(userAuthenticated.getPassword())
                        .accesstoken(jwtConfig.getTokenPrefix() + token)
                        .build();
                responseDTO.setData(loginResponseDTO);
                responseDTO.setSuccessCode(SuccessCode.Login_Success);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("register-user")
    @PermitAll
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody @Validated RegisterRequestDTO registerRequestDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            RegisterResponseDTO registerResponseDTO = userService.createUser(registerRequestDTO);
            responseDTO.setData(registerResponseDTO);
            responseDTO.setSuccessCode(SuccessCode.CREATE_SUCCESS);
        } catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("update-user-info-by-email")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> updateUserInfo(@RequestBody @Validated UpdateUserRequestDTO updateUserRequestDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            UpdateUserResponseDTO updateUserResponseDTO = userService.updateUserInfo(updateUserRequestDTO);
            responseDTO.setData(updateUserResponseDTO);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-user-info-by-email")
    @PreAuthorize("hasAnyRole('CUSTOMER' , 'ADMIN', 'STAFF')")
    public ResponseEntity<ResponseDTO> getUserInfo(@RequestParam @Validated String email) {
        ResponseDTO responseDTO = new ResponseDTO();
        User user = userService.findUserByEmail(email);
        try {
            responseDTO.setData(user);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get_users_has_highest_order_complete")
    @PermitAll
    public ResponseEntity<ResponseDTO> getUsersHasHighestOrder(){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<TopUserResponseDTO> topUserResponseDTOS = userService.getAllUsersHasHighestOrder();
            responseDTO.setData(topUserResponseDTOS);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("change_password")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> changePassword(@RequestBody @Validated ChangePasswordRequestDTO changePasswordRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            ChangePasswordReponseDTO changePasswordReponseDTO = userService.changePassWord(changePasswordRequestDTO);
            responseDTO.setData(changePasswordReponseDTO);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

//    @GetMapping("get-all-user")
//    @PreAuthorize("has('Admin')")
//    public ResponseEntity
}
