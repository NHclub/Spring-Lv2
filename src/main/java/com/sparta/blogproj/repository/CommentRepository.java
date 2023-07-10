package com.sparta.blogproj.repository;

import com.sparta.blogproj.entity.Comment;
import com.sparta.blogproj.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {


}
