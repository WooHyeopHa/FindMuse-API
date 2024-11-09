package com.whh.findmuseapi.review.service;

import com.whh.findmuseapi.review.dto.*;

public interface ReviewService {
    AllReviewResponse getArtReview(long artId, String creteria);

    void createReview(ReviewRequest reviewRequest);

    boolean likeReview(ReviewLikeRequest reviewLikeRequest);

    void likeReviewCancle(long reviewId, long userId);

    void giveReviewStar(ReviewStarRequest reviewStarRequest);

    void updateReview(ReviewUpdateRequest reviewUpdateRequest);

    void deleteReview(long userId, long reviewId);
}
