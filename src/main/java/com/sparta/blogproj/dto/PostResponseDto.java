package com.sparta.blogproj.dto;

import com.sparta.blogproj.entity.Comment;
import com.sparta.blogproj.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentList;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.username = post.getUser().getUsername();
        this.createAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.commentList = post.getCommentList().stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
}
