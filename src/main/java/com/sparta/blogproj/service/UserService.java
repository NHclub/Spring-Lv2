package com.sparta.blogproj.service;

import com.sparta.blogproj.dto.StatusMessageDto;
import com.sparta.blogproj.dto.UserInformationDto;
import com.sparta.blogproj.entity.User;
import com.sparta.blogproj.entity.UserRoleEnum;
import com.sparta.blogproj.exception.CheckUserInformation;
import com.sparta.blogproj.exception.UsernameCheckExistence;
import com.sparta.blogproj.jwt.JwtUtil;
import com.sparta.blogproj.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 회원 가입
    public ResponseEntity<StatusMessageDto> signup(UserInformationDto requestDto) {
        userRepository.findByUsername(requestDto.getUsername()).ifPresent(a -> {
            throw new UsernameCheckExistence("중복된 username 입니다.");
        });
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) { //isAdmin 은 requestDto에 있는 Admin 값이 true인지 false인지 확인해줌. 롬복의 @Getter 메서드는 원래는 get..으로 가져오지만 타입이 boolean 일때는 is.. 를 사용하여 가져옴 그러니 isAdmin 이라는 메서드를 생성하지 않아도 가져올 수 있었던 것!
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }
        User user = new User(username, password, role);
        userRepository.save(user);
        StatusMessageDto statusMessageDto = new StatusMessageDto("회원가입 성공", HttpStatus.OK.value());
        return new ResponseEntity<>(statusMessageDto, HttpStatus.OK);
    }

    // 로그인
    public ResponseEntity<StatusMessageDto> login(UserInformationDto requestDto, HttpServletResponse res) {
        User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(() ->
                new CheckUserInformation("회원을 찾을 수 없습니다."));
        String password = requestDto.getPassword();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CheckUserInformation("회원을 찾을 수 없습니다.");
        }

        String token = jwtUtil.createToken(requestDto.getUsername(), UserRoleEnum.USER);
        res.setHeader(JwtUtil.AUTHORIZATION_HEADER, token);
        StatusMessageDto statusMessageDto = new StatusMessageDto("로그인 성공", HttpStatus.OK.value());
        return new ResponseEntity<>(statusMessageDto, HttpStatus.OK);
    }

}
