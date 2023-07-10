package com.sparta.blogproj.controller;

import com.sparta.blogproj.dto.StatusMessageDto;
import com.sparta.blogproj.dto.UserInformationDto;
import com.sparta.blogproj.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 API
    @PostMapping("/auth/signup")
    public ResponseEntity<StatusMessageDto> createUser(@RequestBody @Valid UserInformationDto requestDto) {
        return userService.signup(requestDto);
    }

    // 로그인 API
    @PostMapping("/auth/login")
    public ResponseEntity<StatusMessageDto> login(@RequestBody UserInformationDto requestDto, HttpServletResponse res) {
        return userService.login(requestDto, res);
    }

}
