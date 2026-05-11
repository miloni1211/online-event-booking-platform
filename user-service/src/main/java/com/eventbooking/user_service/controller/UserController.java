package com.eventbooking.user_service.controller;


import com.eventbooking.user_service.dto.LoginResponse;
import com.eventbooking.user_service.dto.RegisterRequest;
import com.eventbooking.user_service.dto.UserResponse;
import com.eventbooking.user_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request){
        UserResponse response=userService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestParam String email, @RequestParam String password){
        LoginResponse response=userService.login(email, password);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id){
        UserResponse response=userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

}
