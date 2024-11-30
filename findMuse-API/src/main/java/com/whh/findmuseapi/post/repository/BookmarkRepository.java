package com.whh.findmuseapi.post.repository;

import com.whh.findmuseapi.post.entity.Bookmark;
import com.whh.findmuseapi.post.entity.Post;
import com.whh.findmuseapi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findAllByUser(User user);
}