package com.whh.findmuseapi.review.repository;

import com.whh.findmuseapi.art.entity.Art;
import com.whh.findmuseapi.review.entity.ArtReview;
import com.whh.findmuseapi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ArtReview, Long> {
    boolean existsByUserAndArt(User user, Art art);
    ArtReview findByUserAndArt(User user, Art art);
}
