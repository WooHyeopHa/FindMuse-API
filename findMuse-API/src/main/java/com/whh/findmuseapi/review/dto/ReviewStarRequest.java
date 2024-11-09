package com.whh.findmuseapi.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewStarRequest {
    private Long artId;
    private Long userId;
    private float star;
    private boolean isStared; // 리뷰 작성 여부
}
