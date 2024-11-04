package com.whh.findmuseapi.art.repository;

import com.whh.findmuseapi.art.entity.Art;
import com.whh.findmuseapi.art.entity.ArtLike;
import com.whh.findmuseapi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.List;

@Repository
public interface ArtLikeRepository extends JpaRepository<ArtLike, Long> {
    Optional<ArtLike> findArtLikeByArtAndUser(Art art, User user);
    boolean existsByUserAndArt(User user, Art art);
    List<ArtLike> findByUserId(Long userId);
}

