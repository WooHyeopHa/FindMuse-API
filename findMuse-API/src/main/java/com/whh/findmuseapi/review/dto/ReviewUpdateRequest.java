package com.whh.findmuseapi.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewUpdateRequest {
    private Long userId;
    private Long reviewId;
    private String content;
}
