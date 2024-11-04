package com.whh.findmuseapi.review.service;

import com.whh.findmuseapi.review.dto.*;

public interface ReviewService {
    AllReviewResponse getArtReview(Long artId);

    void createReview(ReviewRequest reviewRequest);

    boolean likeReview(ReviewLikeRequest reviewLikeRequest);

    void likeReviewCancle(Long reviewId, Long userId);

    void giveReviewStar(ReviewStarRequest reviewStarRequest);

    void updateReview(ReviewUpdateRequest reviewUpdateRequest);

    void deleteReview(Long userId, Long reviewId);
}
