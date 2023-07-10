package com.sparta.blogproj.service;

import com.sparta.blogproj.dto.PostListResponseDto;
import com.sparta.blogproj.dto.PostRequestDto;
import com.sparta.blogproj.dto.PostResponseDto;
import com.sparta.blogproj.dto.StatusMessageDto;
import com.sparta.blogproj.entity.Post;
import com.sparta.blogproj.entity.User;
import com.sparta.blogproj.entity.UserRoleEnum;
import com.sparta.blogproj.jwt.JwtUtil;
import com.sparta.blogproj.repository.PostRepository;
import com.sparta.blogproj.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public PostService(PostRepository postRepository, UserRepository userRepository, JwtUtil jwtUtil) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // 게시글 작성
    public PostResponseDto createPost(PostRequestDto requestDto, HttpServletRequest req) {
        User user = findUser(req);

        Post post = new Post(requestDto, user);
        Post savePost = postRepository.save(post);
        PostResponseDto postResponseDto = new PostResponseDto(savePost);
        return postResponseDto;
    }

    // 전체 게시글 조회
    public PostListResponseDto getPosts() {
        List<Post> posts = postRepository.findAllByOrderByModifiedAtDesc();
        List<PostResponseDto> postResponseDtoList = posts.stream()
                .map(post -> new PostResponseDto(post))
                .collect(Collectors.toList());
        return new PostListResponseDto(postResponseDtoList);
    }

    // 특정 게시글 조회
    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("게시글이 존재하지 않습니다."));

        return new PostResponseDto(post);
    }

    // 게시글 수정
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, HttpServletRequest req) {
        User user = findUser(req);
        Post userPost = postRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("게시글이 존재하지 않습니다."));
        if (user.getRole().equals(UserRoleEnum.USER)) {
            if (user.getId().equals(userPost.getUser().getId())) {
                userPost.update(requestDto, user);
                return new PostResponseDto(userPost);
            } else {
                throw new IllegalArgumentException("회원님의 게시글이 아닙니다.");
            }
        } else {
            userPost.update(requestDto, user);
            return new PostResponseDto(userPost);
        }
    }

    // 게시글 삭제
    @Transactional
    public ResponseEntity<StatusMessageDto> deletePost(Long id, HttpServletRequest req) {
        User user = findUser(req);
        Post userPost = postRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("게시글이 존재하지 않습니다."));
        if (user.getRole().equals(UserRoleEnum.USER)) {
            if (user.getId().equals(userPost.getUser().getId())) {
                postRepository.delete(userPost);
                StatusMessageDto statusMessageDto = new StatusMessageDto("게시글 삭제 성공", HttpStatus.OK.value());
                return new ResponseEntity<>(statusMessageDto, HttpStatus.OK);
            } else {
                throw new IllegalArgumentException("회원님의 게시글이 아닙니다.");
            }
        } else {
            postRepository.delete(userPost);
            StatusMessageDto statusMessageDto = new StatusMessageDto("게시글 삭제 성공", HttpStatus.OK.value());
            return new ResponseEntity<>(statusMessageDto, HttpStatus.OK);
        }
    }

    // 토큰 검사 후 User 반환
    private User findUser(HttpServletRequest req) {
        String token = jwtUtil.getJwtFromHeader(req);

        if (jwtUtil.validateToken(token)) {
            Claims claims = jwtUtil.getUserInfoFromToken(token);
            String username = claims.get("sub", String.class);
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
        } else {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }

}
