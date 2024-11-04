package com.whh.findmuseapi.review.repository;

import com.whh.findmuseapi.review.entity.ArtReview;
import com.whh.findmuseapi.review.entity.ArtReviewLike;
import com.whh.findmuseapi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ArtReviewLike, Long> {
    boolean existsByUserAndArtReview(User user, ArtReview artReview);
    void deleteArtReviewLikeByUserAndArtReview(User user, ArtReview artReview);
}
