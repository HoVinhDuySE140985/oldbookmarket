package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.Jwt.JwtConfig;
import com.example.oldbookmarket.dto.request.UserRequestDTO.LoginRequestDTO;
import com.example.oldbookmarket.dto.request.UserRequestDTO.RegisterRequestDTO;
import com.example.oldbookmarket.dto.request.UserRequestDTO.UpdateUserRequestDTO;
import com.example.oldbookmarket.dto.respone.LoginResponseDTO;
import com.example.oldbookmarket.dto.respone.RegisterResponseDTO;
import com.example.oldbookmarket.dto.respone.ResponseDTO;
import com.example.oldbookmarket.dto.respone.UpdateUserResponseDTO;
import com.example.oldbookmarket.entity.User;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.UserService;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Date;

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

    public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());

        try {
            Authentication authenticate = authenticationManager.authenticate(authentication);
            System.out.println(authenticate.getName());

            if(authenticate.isAuthenticated()){
                User userAuthenticated = userService.findByEmail(authenticate.getName());
                String token = Jwts.builder().setSubject(authenticate.getName())
                        .claim(("authorities"), authenticate.getAuthorities())
                        .claim("id", userAuthenticated.getId())
                        .setIssuedAt((new Date())).setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                        .signWith(jwtConfig.secretKey()).compact();

                LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                        .id(userAuthenticated.getId())
                        .name(userAuthenticated.getName())
//                        .role(userAuthenticated.getRole().getId())
                        .token(jwtConfig.getTokenPrefix() + token)
                        .build();
                responseDTO.setData(loginResponseDTO);
                responseDTO.setSuccessCode(SuccessCode.Login_Success);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("register-user")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            User user = userService.findByEmail(registerRequestDTO.getEmail());
            if(user == null){
                RegisterResponseDTO registerResponseDTO = userService.createUser(registerRequestDTO);
                responseDTO.setData(registerResponseDTO);
                responseDTO.setSuccessCode(SuccessCode.CREATE_SUCCESS);
            }else {
                throw new ResponseStatusException(HttpStatus.valueOf(400),"USER_EXISTED");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("update-user-infor")
    public ResponseEntity<ResponseDTO> updateUserInfo(@RequestBody UpdateUserRequestDTO updateUserRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            UpdateUserResponseDTO updateUserResponseDTO = userService.updateUserInfo(updateUserRequestDTO);

        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-user-info/{userId}")
    public ResponseEntity<ResponseDTO> getUserInfo(@PathVariable Long id){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            User user = userService.findUserById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);

    }
}
