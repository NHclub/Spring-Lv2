package com.sparta.blogproj.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long postId;
    private String content;

}
