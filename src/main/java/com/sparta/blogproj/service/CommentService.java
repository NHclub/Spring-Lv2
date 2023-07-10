package com.sparta.blogproj.service;

import com.sparta.blogproj.dto.CommentRequestDto;
import com.sparta.blogproj.dto.CommentResponseDto;
import com.sparta.blogproj.dto.CommentUpdateDto;
import com.sparta.blogproj.dto.StatusMessageDto;
import com.sparta.blogproj.entity.Comment;
import com.sparta.blogproj.entity.Post;
import com.sparta.blogproj.entity.User;
import com.sparta.blogproj.entity.UserRoleEnum;
import com.sparta.blogproj.jwt.JwtUtil;
import com.sparta.blogproj.repository.CommentRepository;
import com.sparta.blogproj.repository.PostRepository;
import com.sparta.blogproj.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository, JwtUtil jwtUtil) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // 댓글 작성
    public CommentResponseDto createComment(CommentRequestDto requestDto, HttpServletRequest req) {
        User user = findUser(req);
        Post userpost = postRepository.findById(requestDto.getPostId()).orElseThrow(() ->
                new NoSuchElementException("게시글이 존재하지 않습니다."));
        Comment comment = new Comment(requestDto, user, userpost);
        Comment saveComment = commentRepository.save(comment);
        CommentResponseDto commentResponseDto = new CommentResponseDto(saveComment);
        return commentResponseDto;
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long id, CommentUpdateDto requestDto, HttpServletRequest req) {
        User user = findUser(req);
        Comment userComment = commentRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("댓글이 존재하지 않습니다."));
        if (user.getRole().equals(UserRoleEnum.USER)) {
            if (user.getId().equals(userComment.getUser().getId())) {
                userComment.update(requestDto);
                return new CommentResponseDto(userComment);
            }else{
                throw new IllegalArgumentException("회원님의 댓글이 아닙니다.");
            }
        }else{
            userComment.update(requestDto);
            return new CommentResponseDto(userComment);
        }
    }

    // 댓글 삭제
    @Transactional
    public ResponseEntity<StatusMessageDto> deletePost(Long id, HttpServletRequest req) {
        User user = findUser(req);
        Comment userComment = commentRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("댓글이 존재하지 않습니다."));
        if (user.getRole().equals(UserRoleEnum.USER)) {
            if (user.getId().equals(userComment.getUser().getId())) {
                commentRepository.delete(userComment);
                StatusMessageDto statusMessageDto = new StatusMessageDto("댓글 삭제 성공", HttpStatus.OK.value());
                return new ResponseEntity<>(statusMessageDto,HttpStatus.OK);
            }else{
                throw new IllegalArgumentException("회원님의 댓글이 아닙니다.");
            }
        }else{
            commentRepository.delete(userComment);
            StatusMessageDto statusMessageDto = new StatusMessageDto("댓글 삭제 성공", HttpStatus.OK.value());
            return new ResponseEntity<>(statusMessageDto,HttpStatus.OK);
        }
    }

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
