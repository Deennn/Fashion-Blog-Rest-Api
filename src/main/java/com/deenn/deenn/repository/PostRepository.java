package com.deenn.deenn.repository;

import com.deenn.deenn.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
