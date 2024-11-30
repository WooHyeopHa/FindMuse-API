package com.whh.findmuseapi.post.dto.response;

import lombok.Getter;

@Getter
public class PostCreateResponse {

    private final Long postId;

    public PostCreateResponse(Long postId) {
        this.postId = postId;
    }
}
