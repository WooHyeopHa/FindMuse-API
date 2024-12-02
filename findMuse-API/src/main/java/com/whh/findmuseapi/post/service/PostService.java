package com.whh.findmuseapi.post.service;

import com.whh.findmuseapi.post.dto.request.PostCreateRequest;
import com.whh.findmuseapi.post.dto.request.PostUpdateRequest;
import com.whh.findmuseapi.post.dto.response.PostCreateResponse;
import com.whh.findmuseapi.post.dto.response.PostListResponse;
import com.whh.findmuseapi.post.dto.response.PostOneResponse;

public interface PostService {

    /**
     * 게시물 생성 로직
     */
    PostCreateResponse createPost(PostCreateRequest createRequest);

    /**
     * 게시물 단일 조회
     */
    PostOneResponse getPost(long postId, long userId);

    /**
     * 게시물 수정
     */
    void updatePost(PostUpdateRequest updateRequest);

    /**
     * 게시글 삭제
     */
    void deletePost(long userId, long postId);

    /**
     * 모집글 리스트 조회
     */
    PostListResponse getPostList(long userId, String creteria);

    /**
     * 북마크 등록
     */
    void doBookmark(long userId, long postId);

    /**
     * 북마크 해제
     */
    void cancleBookmark(long userId, long postId);
}
