package com.whh.findmuseapi.post.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostListResponse {
    private List<PostListDetailResponse> postList;

    public static PostListResponse toDto(List<PostListDetailResponse> postList) {
        return PostListResponse.builder()
                .postList(postList)
                .build();
    }
}
