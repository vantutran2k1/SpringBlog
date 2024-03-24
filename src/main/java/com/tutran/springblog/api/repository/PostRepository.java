package com.tutran.springblog.api.repository;

import com.tutran.springblog.api.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    boolean existsByTitle(String title);

    @EntityGraph(attributePaths = {"comments"})
    Optional<Post> findById(long id);

    Page<Post> findByCategoryId(long categoryId, Pageable pageable);
}