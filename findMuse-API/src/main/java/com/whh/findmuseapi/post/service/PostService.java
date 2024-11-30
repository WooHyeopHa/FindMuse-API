package com.whh.findmuseapi.post.service;

import com.whh.findmuseapi.post.dto.request.PostCreateRequest;
import com.whh.findmuseapi.post.dto.request.PostUpdateRequest;
import com.whh.findmuseapi.post.dto.response.PostCreateResponse;
import com.whh.findmuseapi.post.dto.response.PostListResponse;
import com.whh.findmuseapi.post.dto.response.PostOneReadResponse;
import com.whh.findmuseapi.post.entity.Post;
import com.whh.findmuseapi.user.entity.User;

/**
 * class: PostService.
 * 게시글 관련 서비스 입니다.
 *
 * @author devminseo
 * @version 8/20/24
 */

public interface PostService {

    /**
     * 게시물 생성 로직
     */
    PostCreateResponse createPost(PostCreateRequest createRequest);

    /**
     * 게시물 단일 조회 로직
     */
    PostOneResponse getPost(long postId, long userId);

    /**
     * 게시물 수정 로직입니다.
     *
     * @param updateRequest 수정 사항
     */
    void updatePost(PostUpdateRequest updateRequest);

    /**
     * 게시글 삭제
     */
    void deletePost(long userId, long postId);

    /**
     * 모집글 리스트를 가져옵니다.
     */
    PostListResponse getPostList(long userId, String creteria);

}
