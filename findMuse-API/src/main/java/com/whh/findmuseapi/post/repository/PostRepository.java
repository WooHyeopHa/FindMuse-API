package com.whh.findmuseapi.post.repository;

import com.whh.findmuseapi.post.entity.Post;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select p from Post p where p.id = :postId")
    Optional<Post> findByIdWithLock(Long postId);

    List<Post> findAllByOrderByCreateDateDesc();

    List<Post> findAllByOrderByBookmarkCntDesc();
}