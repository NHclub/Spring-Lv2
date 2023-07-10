package com.sparta.blogproj.controller;

import com.sparta.blogproj.dto.*;
import com.sparta.blogproj.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 작성
    @PostMapping("/comment")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto requestDto, HttpServletRequest req) {
        return commentService.createComment(requestDto, req);
    }

    // 댓글 수정
    @PutMapping("comment/{id}")
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentUpdateDto requestDto, HttpServletRequest req) {
        return commentService.updateComment(id, requestDto, req);
    }

    // 댓글 삭제
    @DeleteMapping("comment/{id}")
    public ResponseEntity<StatusMessageDto> deleteComment(@PathVariable Long id, HttpServletRequest req) {
        return commentService.deletePost(id, req);
    }

}
