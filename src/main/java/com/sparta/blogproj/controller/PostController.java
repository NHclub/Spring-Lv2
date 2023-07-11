package com.sparta.blogproj.controller;

import com.sparta.blogproj.dto.PostListResponseDto;
import com.sparta.blogproj.dto.PostRequestDto;
import com.sparta.blogproj.dto.PostResponseDto;
import com.sparta.blogproj.dto.StatusMessageDto;
import com.sparta.blogproj.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;


    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 전체 게시글 목록 조회 API
    @GetMapping("/posts")
    public PostListResponseDto getPosts() {
        return postService.getPosts();
    }

    // 게시글 작성 API
    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest req) {
        return postService.createPost(requestDto, req);
    }

    // 선택한 게시글 조회 API
    @GetMapping("/post/{id}")
    public PostResponseDto getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    //선택한 게시글 수정 API
    @PutMapping("/post/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest req) {

        return postService.updatePost(id, requestDto, req);
    }

    // 선택한 게시글 삭제 API
    @DeleteMapping("/post/{id}")
    public ResponseEntity<StatusMessageDto> deletePost(@PathVariable Long id, HttpServletRequest req) {
        return postService.deletePost(id, req);
    }

}
