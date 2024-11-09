package com.whh.findmuseapi.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewRequest {
    private Long artId;
    private Long userId;
    private String content;
}
