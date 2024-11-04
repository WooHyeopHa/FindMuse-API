package com.whh.findmuseapi.review.controller;

import com.whh.findmuseapi.common.constant.ResponseCode;
import com.whh.findmuseapi.common.util.ApiResponse;
import com.whh.findmuseapi.review.dto.ReviewLikeRequest;
import com.whh.findmuseapi.review.dto.ReviewRequest;
import com.whh.findmuseapi.review.dto.ReviewStarRequest;
import com.whh.findmuseapi.review.dto.ReviewUpdateRequest;
import com.whh.findmuseapi.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ReviewContoller {

    private final ReviewService reviewService;
    /**
     * 문화예술 별 리뷰 종합
     */
    @GetMapping("/review/{artId}}")
    public ApiResponse<?> getArtReview(@PathVariable Long artId, @RequestParam(required = false, defaultValue = "date", value = "sort") String creteria) {
       return ApiResponse.createSuccess(ResponseCode.SUCCESS, reviewService.getArtReview(artId, creteria));
    }

    /**
     * 리뷰 작성
     */
    @PostMapping("/review")
    public ApiResponse<?> createReview(@RequestBody ReviewRequest reviewRequest) {
        reviewService.createReview(reviewRequest);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.RESOURCE_CREATED);
    }

    /**
     * 리뷰 수정
     */
    @PutMapping("/review")
    public ApiResponse<?> updateReview(@RequestBody ReviewUpdateRequest reviewUpdateRequest) {
        reviewService.updateReview(reviewUpdateRequest);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.SUCCESS);
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/review/{userId}/{reviewId}")
    public ApiResponse<?> deleteReview(@PathVariable Long userId, @PathVariable Long reviewId) {
        reviewService.deleteReview(userId, reviewId);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.SUCCESS);
    }

    /**
     * 리뷰 공감하기
     */
    @PatchMapping("/review/like")
    public ApiResponse<?> likeReview(@RequestBody ReviewLikeRequest reviewLikeRequest) {
        boolean result = reviewService.likeReview(reviewLikeRequest);
        return ApiResponse.createSuccess(ResponseCode.SUCCESS, result);
    }

    /**
     * 리뷰 공감 취소하기
     */
    @DeleteMapping("/review/like/{reviewId}/{userId}")
    public ApiResponse<?> likeReviewCancle(@PathVariable Long reviewId, @PathVariable Long userId) {
        reviewService.likeReviewCancle(reviewId, userId);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.SUCCESS);
    }

    /**
     * 별점 작성
     */
    @PostMapping("/review/star")
    public ApiResponse<?> giveReviewStar(@RequestBody ReviewStarRequest reviewStarRequest) {
        reviewService.giveReviewStar(reviewStarRequest);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.SUCCESS);
    }

}
