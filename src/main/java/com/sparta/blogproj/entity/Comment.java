package com.sparta.blogproj.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.blogproj.dto.CommentRequestDto;
import com.sparta.blogproj.dto.CommentUpdateDto;
import com.sparta.blogproj.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Setter
@Table(name = "comment") // 매핑할 테이블의 이름을 지정
@NoArgsConstructor
public class Comment extends Timestamped  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Comment(CommentRequestDto requestDto, User user, Post post) {
        this.content = requestDto.getContent();
        this.user = user;
        this.post = post;
    }

    public void update(CommentUpdateDto requestDto) {
        this.content = requestDto.getContent();
    }
}