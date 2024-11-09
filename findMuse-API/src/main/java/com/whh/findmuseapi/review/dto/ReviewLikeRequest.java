package com.whh.findmuseapi.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewLikeRequest {
    private Long userId;
    private Long reviewId;
}
