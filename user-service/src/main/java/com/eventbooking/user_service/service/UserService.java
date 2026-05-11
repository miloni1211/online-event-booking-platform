package com.eventbooking.user_service.service;

import com.eventbooking.user_service.config.JwtUtil;
import com.eventbooking.user_service.dto.LoginResponse;
import com.eventbooking.user_service.dto.RegisterRequest;
import com.eventbooking.user_service.dto.UserResponse;
import com.eventbooking.user_service.model.User;
import com.eventbooking.user_service.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {

        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }
    //user should be able to register and login

    public UserResponse register(RegisterRequest request){
        Optional<User> existingUser=userRepository.findByEmail(request.getEmail());

        if(existingUser.isPresent()){
            throw new RuntimeException("Emailid already registered");
        }

        User user=new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);

    }

    public LoginResponse login(String email, String password){

        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));

        if(!user.getPassword().equals(password)){
            throw new RuntimeException("Invalid Password");
        }

        // 3. WHY: Generate JWT token with email and role
        // Client will use this token for all future requests
        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getId()
        );

        // 4. Return token + user details
        return new LoginResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    public UserResponse getUserById(String id){
       User user=userRepository.findById(id).orElseThrow(()->new RuntimeException("User Not Found"));

       return mapToResponse(user);
    }



    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole()
        );
    }
}
