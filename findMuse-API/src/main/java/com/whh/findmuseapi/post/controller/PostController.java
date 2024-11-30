package com.whh.findmuseapi.post.controller;

import com.whh.findmuseapi.common.constant.ResponseCode;
import com.whh.findmuseapi.common.util.ApiResponse;
import com.whh.findmuseapi.post.dto.request.PostCreateRequest;
import com.whh.findmuseapi.post.dto.request.PostUpdateRequest;
import com.whh.findmuseapi.post.dto.response.PostCreateResponse;
import com.whh.findmuseapi.post.dto.response.PostListResponse;
import com.whh.findmuseapi.post.dto.response.PostOneResponse;
import com.whh.findmuseapi.post.service.PostService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    /**
     * 모집글 생성
     */
    @PostMapping
    public ApiResponse<?> createPost(@Valid @RequestBody PostCreateRequest createRequest) {
        PostCreateResponse response = postService.createPost(createRequest);
        return ApiResponse.createSuccess(ResponseCode.RESOURCE_CREATED, response);
    }

    /**
     * 모집글 목록 조회
     */
    @GetMapping("/{userId}")
    public ApiResponse<?> getPostList(@PathVariable(value = "userId") long userId,
                                      @RequestParam(value = "sort", defaultValue = "최신순") String creteria) {
        PostListResponse postList = postService.getPostList(userId, creteria);
        return ApiResponse.createSuccess(ResponseCode.SUCCESS, postList);
    }

    /**
     * 모집글 단일 조회
     */
    @GetMapping("/{postId}")
    public ApiResponse<?> readPost(@PathVariable long postId,
                                   @RequestParam(value = "userId") long userId) {
        PostOneResponse postResponse = postService.getPost(postId, userId);
        return ApiResponse.createSuccess(ResponseCode.SUCCESS, postResponse);
    }

    /**
     * 모집글 수정
     */
    @PutMapping
    public ApiResponse<?>  updatePost(@Valid @RequestBody PostUpdateRequest updateRequest) {
        postService.updatePost(updateRequest);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.SUCCESS);
    }

    /**
     * 모집글 삭제
     */
    @DeleteMapping("/{postId}")
    public ApiResponse<?>  deletePost(@PathVariable long postId,
                                      @RequestParam long userId) {
        postService.deletePost(userId, postId);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.SUCCESS);
    }
}
