package com.example.oldbookmarket.controller;

import java.time.LocalDate;
import java.util.Date;

import com.example.oldbookmarket.dto.response.userDTO.UserLoginResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.oldbookmarket.Jwt.JwtConfig;
import com.example.oldbookmarket.dto.request.userDTO.RegisterRequestDTO;
import com.example.oldbookmarket.dto.response.ResponseDTO;
import com.example.oldbookmarket.dto.response.userDTO.LoginResponseDTO;
import com.example.oldbookmarket.entity.User;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.models.OAuth2Request;
import com.example.oldbookmarket.service.serviceinterface.UserService;

import io.jsonwebtoken.Jwts;

@RestController
@RequestMapping("/api/auth")
public class OAuth2Controller {

    private final Logger logger = LoggerFactory.getLogger(OAuth2Controller.class);

    @Autowired
    UserService userService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtConfig jwtConfig;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login_google")
    public ResponseEntity<ResponseDTO> authenticate(@RequestBody @Validated OAuth2Request data) {
        // GoogleCredentials credentials = new GoogleCredentials(null)
        ResponseDTO responseDTO = new ResponseDTO();
        logger.info("The Id = " + data.getId());
        logger.info("fullname = " + data.getFullname());
        logger.info("Email = " + data.getEmail());
        Authentication authentication = null;
        if (!userService.isExistUserByEmail(data.getEmail())) {
            // User user = new User();
            // user.setEmail(data.getEmail());
            // user.setName(data.getFullname());
            // user.setImageUrl(data.getImageUrl());
            // user.setPassword(data.getId());
            RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(data.getFullname(), data.getEmail(), "",
                    data.getId());
            userService.createUser(registerRequestDTO);

            authentication = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getId());

        }
        authentication = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getId());
        try {
            Authentication authenticate = authenticationManager.authenticate(authentication);
            System.out.println(authenticate.getName());
            if (authenticate.isAuthenticated()) {
                UserLoginResponseDTO userAuthenticated = userService.findByEmail(authenticate.getName());
                String token = Jwts.builder().setSubject(authenticate.getName())
                        .claim(("authorities"), authenticate.getAuthorities())
                        .claim("id", userAuthenticated.getId())
                        .setIssuedAt((new Date())).setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                        .signWith(jwtConfig.secretKey()).compact();

                logger.info("token is = " + token);
                LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                        .id(userAuthenticated.getId())
                        .name(userAuthenticated.getName())
                        .email(userAuthenticated.getEmail())
                        .phoneNumber(userAuthenticated.getPhoneNumber())
                        .gender(userAuthenticated.getGender())
                        .imageUrl(userAuthenticated.getImageUrl())
                        .dob(userAuthenticated.getDob())
                        .password(userAuthenticated.getPassword())
                        .status(userAuthenticated.getStatus())
                        .accesstoken(jwtConfig.getTokenPrefix() + token)
                        .build();
                responseDTO.setData(loginResponseDTO);
                responseDTO.setSuccessCode(SuccessCode.Login_Success);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(responseDTO, org.springframework.http.HttpStatus.OK);
    }

}
