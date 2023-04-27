package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.userDTO.ChangePasswordRequestDTO;
import com.example.oldbookmarket.dto.request.userDTO.ForgotPasswordRequestDTO;
import com.example.oldbookmarket.dto.request.userDTO.RegisterRequestDTO;
import com.example.oldbookmarket.dto.request.userDTO.UpdateUserRequestDTO;
import com.example.oldbookmarket.dto.response.emailDTO.EmailResponseDTO;
import com.example.oldbookmarket.dto.response.userDTO.*;
import com.example.oldbookmarket.entity.Post;
import com.example.oldbookmarket.entity.User;
//import com.example.oldbookmarket.entity.UserStatus;
import com.example.oldbookmarket.entity.Wallet;
import com.example.oldbookmarket.repository.*;
//import com.example.oldbookmarket.service.serviceinterface.EmailService;
import com.example.oldbookmarket.service.serviceinterface.EmailService;
import com.example.oldbookmarket.service.serviceinterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder encoder;


    @Autowired
    RoleRepo roleRepo;

    @Autowired
    WalletRepo walletRepo;

    @Autowired
    PostRepo postRepo;

    @Autowired
    EmailService emailService;


    @Override
    public UserLoginResponseDTO findByEmail(String email) {
        UserLoginResponseDTO responseDTO = null;
        try {
            User user = userRepo.findUserByEmail(email);
            responseDTO = UserLoginResponseDTO.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .dob(user.getDob())
                    .imageUrl(user.getImageUrl())
                    .name(user.getName())
                    .status(user.getUserStatus())
                    .gender(user.getGender())
                    .password(user.getPassword())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseDTO;
    }

    @Override
    public RegisterResponseDTO createUser(RegisterRequestDTO registerRequestDTO) {
        RegisterResponseDTO registerResponseDTO = null;
        try {
            User user = userRepo.findUserByEmail(registerRequestDTO.getEmail());
            if (user == null) {
                user = new User();
                user.setName(registerRequestDTO.getName());
                user.setEmail(registerRequestDTO.getEmail());
                user.setPhoneNumber(registerRequestDTO.getPhoneNumber());
                user.setPassword(encoder.encode(registerRequestDTO.getPassword()));
                user.setUserStatus("active");
                user.setRole(roleRepo.findByName("CUSTOMER"));
                user = userRepo.save(user);

                Wallet wallet = Wallet.builder()
                        .user(user)
                        .amount(BigDecimal.valueOf(0))
                        .build();
                walletRepo.save(wallet);

                registerResponseDTO = RegisterResponseDTO.builder()
                        .userId(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .password(user.getPassword())
                        .build();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(415), "USER_EXISTED");
        }
        return registerResponseDTO;
    }

    @Override
    public UpdateUserResponseDTO updateUserInfo(UpdateUserRequestDTO updateUserRequestDTO) {
        UpdateUserResponseDTO updateUserResponseDTO = null;
        try {
            User user = userRepo.findUserByEmail(updateUserRequestDTO.getEmail());
            if (user != null) {
                user.setName(updateUserRequestDTO.getName());
                user.setImageUrl(updateUserRequestDTO.getImageUrl());
                user.setPhoneNumber(updateUserRequestDTO.getPhoneNumber());
                user.setDob(updateUserRequestDTO.getDob());
                user.setGender(updateUserRequestDTO.getGender());
                user = userRepo.save(user);
                updateUserResponseDTO = UpdateUserResponseDTO.builder()
                        .name(user.getName())
                        .email(user.getEmail())
                        .imageUrl(user.getImageUrl())
                        .phoneNumber(user.getPhoneNumber())
                        .dob(user.getDob())
                        .gender(user.getGender())
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateUserResponseDTO;
    }

    @Override
    public User findUserByEmail(String email) {
        User user = new User();
        try {
            user = userRepo.findUserByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

    @Override
    public List<TopUserResponseDTO> getAllUsersHasHighestOrder() {
        List<TopUserResponseDTO> topUserResponseDTOS = new ArrayList<>();
        try {
            topUserResponseDTOS = userRepo.findUsersHasHighestOrder();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topUserResponseDTOS;
    }


    public ChangePasswordReponseDTO changePassWord(ChangePasswordRequestDTO changePasswordRequestDTO) {
        ChangePasswordReponseDTO changePasswordReponseDTO = null;
        try {
            User user = userRepo.findUserByEmail(changePasswordRequestDTO.getEmail());
            Boolean check = encoder.matches(changePasswordRequestDTO.getOldPassword(), user.getPassword());
            if (check) {
                if (changePasswordRequestDTO.getNewPassword().equalsIgnoreCase(changePasswordRequestDTO.getConfirmPassword())) {
                    user.setPassword(encoder.encode(changePasswordRequestDTO.getNewPassword()));
                    user = userRepo.save(user);
                    changePasswordReponseDTO = ChangePasswordReponseDTO.builder()
                            .userId(user.getId())
                            .newPassword(user.getPassword())
                            .build();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return changePasswordReponseDTO;
    }

    public Boolean isExistUserByEmail(String email) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'isExistUserByEmail'");
        User user = userRepo.findUserByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    @Override
    public List<UserResponseDTO> getAllUser(String email) {
        List<User> userList = null;
        List<UserResponseDTO> userResponseDTOS = new ArrayList<>();
        try {
            if (email.equalsIgnoreCase("null")){
                userList = userRepo.findAll();
                for (User user : userList) {
                    UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .name(user.getName())
                            .userImage(user.getImageUrl())
                            .phoneNumber(user.getPhoneNumber())
                            .userStatus(user.getUserStatus())
                            .build();
                    userResponseDTOS.add(userResponseDTO);
                }
            }else {
                User user = userRepo.findUserByEmail(email);
                UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .name(user.getName())
                        .userImage(user.getImageUrl())
                        .phoneNumber(user.getPhoneNumber())
                        .userStatus(user.getUserStatus())
                        .build();
                userResponseDTOS.add(userResponseDTO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userResponseDTOS;
    }

    @Override
    public Boolean banUser(String email) {
        try {
            User user = userRepo.findUserByEmail(email);
            if (user.getUserStatus().equalsIgnoreCase("active")) {
                user.setUserStatus("deactive");
                userRepo.save(user);
                List<Post> postList = postRepo.findAllByUser_Id(user.getId());
                for (Post post : postList) {
                    if (post.getPostStatus().equalsIgnoreCase("active")) {
                        post.setPostStatus("deactive");
                        postRepo.save(post);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User forgotPassword(ForgotPasswordRequestDTO forgotPasswordRequestDTO) {
        User user = null;
        try {
            user = userRepo.findUserByEmail(forgotPasswordRequestDTO.getEmail());
            if (user.getVerificationCode().equalsIgnoreCase(forgotPasswordRequestDTO.getVerificationCode())){
                user.setPassword(encoder.encode(forgotPasswordRequestDTO.getNewPassword()));
                user.setVerificationCode("null");
                userRepo.save(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public String sendVerificationCode(String email) {
        String verificationCodes = randomPassword()+"OBM";
        User user = userRepo.findUserByEmail(email);
        user.setVerificationCode(verificationCodes);
        userRepo.save(user);
        try {
            EmailResponseDTO emailDetail = EmailResponseDTO.builder()
                    .email(email)
                    .subject("Mã Xác Nhận Đổi Mật Khẩu " + email)
                    .massage("Xin chào " + user.getName() + ",\n" +
                            "\n" +
                            "Chúng tôi xin gửi đến bạn mã xác thực \n" +
                            "\n" +
                            "Mã Xác Thực: " + verificationCodes + "\n" +
                            "\n" +
                            "Vui lòng không cung cấp mã xác thực cho người khác!\n" +
                            "\n" +
                            "Trân trọng,\n" +
                            "\n" +
                            "Phòng hỗ trợ khách hàng.\n" +
                            "(Đây là email được gửi tự động, Quý khách vui lòng không hồi đáp theo địa chỉ email này.)")
                    .build();
            emailService.sendSimpleMail(emailDetail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return verificationCodes;
    }

    public String randomPassword() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }

    @Override
    public User findUserByName(String name) {
        User user = null;
        try {
            user = userRepo.findUserByName(name);
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

//    @Override
//    public User searchEmailByKeyWord(String email) {
//        User user = null;
//        try {
//            user = userRepo.findUserByEmail(email);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return user;
//    }
}
