package com.sparta.blogproj.dto;

import com.sparta.blogproj.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private String username;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.username = comment.getUser().getUsername();
        this.createAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
