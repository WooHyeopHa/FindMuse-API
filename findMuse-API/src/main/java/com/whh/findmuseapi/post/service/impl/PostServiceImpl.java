package com.whh.findmuseapi.post.service.impl;

import com.whh.findmuseapi.art.entity.Art;
import com.whh.findmuseapi.art.repository.ArtRepository;
import com.whh.findmuseapi.common.annotation.Retry;
import com.whh.findmuseapi.common.constant.Infos;
import com.whh.findmuseapi.common.exception.CBadRequestException;
import com.whh.findmuseapi.common.exception.CNotFoundException;
import com.whh.findmuseapi.common.exception.CUnAuthorizationException;
import com.whh.findmuseapi.post.dto.request.PostCreateRequest;
import com.whh.findmuseapi.post.dto.request.PostUpdateRequest;
import com.whh.findmuseapi.post.dto.response.PostCreateResponse;
import com.whh.findmuseapi.post.dto.response.PostListDetailResponse;
import com.whh.findmuseapi.post.dto.response.PostListResponse;
import com.whh.findmuseapi.post.dto.response.PostOneResponse;
import com.whh.findmuseapi.post.entity.*;
import com.whh.findmuseapi.post.repository.*;
import com.whh.findmuseapi.post.service.PostService;
import com.whh.findmuseapi.user.entity.User;
import com.whh.findmuseapi.user.repository.UserRepository;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ArtRepository artRepository;
    private final TagRepository tagRepository;
    private final VolunteerRepository volunteerRepository;
    private final BookmarkRepository bookmarkRepository;

    /**
     * 게시글 작성
     */
    @Override
    @Transactional
    public PostCreateResponse createPost(PostCreateRequest createRequest) {
        User user = userRepository.findById(createRequest.getUserId())
                .orElseThrow(() -> new CNotFoundException("회원: " + createRequest.getUserId()));
        Art art = artRepository.findById(createRequest.getArtId())
                .orElseThrow(() -> new CNotFoundException("전시회: " + createRequest.getArtId()));

        List<Tag> tagList = createRequest.getTagList().stream()
                .map(tagName -> tagRepository.findByName(tagName)
                        .orElseThrow(() -> new CNotFoundException("태그: " + tagName)))
                .toList();

        Post post = Post.toEntity(createRequest, art, user);
        tagList.stream().map(tag -> PostTag.builder().post(post).tag(tag).build()).toList();

        return new PostCreateResponse(postRepository.save(post).getId());
    }

    /**
     * 모집글 단건 조회
     */
    @Override
    public PostOneResponse getPost(long postId, long userId) {
        Post post = plusAndGetPost(postId);
        boolean isWriter = userId == (post.getUser().getId());
        log.info("[Post : {}] is Wriiten By {} ? : {}", postId, userId, isWriter);

        int invitedCount = Math.toIntExact(volunteerRepository.countByPostAndStatus(post, Infos.InvieteStatus.ACCESS));
        return PostOneResponse.toDto(post, invitedCount, isWriter);
    }

    @Transactional(timeout = 5)
    public Post plusAndGetPost(long postId) {
        Post post = postRepository.findByIdWithLock(postId).orElseThrow(() -> new CNotFoundException("모집글: " + postId));
        post.viewCountPlusOne();
        return post;
    }

    /**
     * 모집글 수정
     */
    @Override
    @Transactional
    public void updatePost(PostUpdateRequest updateRequest) {
        User writer = userRepository.findById(updateRequest.getUserId())
                .orElseThrow(() -> new CNotFoundException("회원: " + updateRequest.getUserId()));
        Post post = postRepository.findById(updateRequest.getPostId())
                .orElseThrow(() -> new CNotFoundException("게시글: " + updateRequest.getPostId()));

        //더블체크
        checkWriter(writer, post);

        Art art = artRepository.findById(updateRequest.getArtId())
                .orElseThrow(() -> new CNotFoundException("전시회: " + updateRequest.getArtId()));

        List<Tag> tagList = updateRequest.getTagList().stream()
                .map(tagName -> tagRepository.findByName(tagName)
                        .orElseThrow(() -> new CNotFoundException("태그: " + tagName)))
                .toList();

        post.updatePost(updateRequest, art);
        tagList.forEach(tag -> PostTag.builder()
                        .post(post)
                        .tag(tag).build());
    }

    /**
     * 게시글 삭제
     */
    @Override
    @Transactional
    public void deletePost(long userId, long postId) {
        User writer = userRepository.findById(userId).orElseThrow(() -> new CNotFoundException("회원: " + userId));
        Post post = postRepository.findById(postId).orElseThrow(() -> new CNotFoundException("게시글: " + postId));

        //더블 체크
        checkWriter(writer, post);
        postRepository.delete(post);
    }

    /**
     * 모집글 목록 조회
     */
    @Override
    public PostListResponse getPostList(long userId, String creteria) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new CNotFoundException("회원: " + userId));
        Infos.SortType sortType = Infos.SortType.convertStringToSortType(creteria);

        List<Post> postList = new ArrayList<>();
        if (sortType.equals(Infos.SortType.LATEST)) {
            postList = postRepository.findAllByOrderByCreateDateDesc();
        }
        else if(sortType.equals(Infos.SortType.POPULAR)){
            postList = postRepository.findAllByOrderByBookmarkCntDesc();
        }

        List<Bookmark> bookmarkByMe = bookmarkRepository.findAllByUser(findUser);
        return PostListResponse.toDto(postList.stream()
                .map(p -> {
                    if (bookmarkByMe.stream()
                            .anyMatch(b -> b.getPost().equals(p))) {
                        return PostListDetailResponse.toDto(p, true);
                    }
                    return PostListDetailResponse.toDto(p, false);
                })
                .collect(Collectors.toList()));
    }

    /**
     * 북마크 등록
     */
    @Retry
    @Transactional
    @Override
    public void doBookmark(long userId, long postId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new CNotFoundException("회원: " + userId));
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new CNotFoundException("게시글: " + postId));
        Bookmark findBookmark = bookmarkRepository.findByPostAndUser(findPost, findUser)
                .orElse(new Bookmark(findPost, findUser));
        findBookmark.changeStatus();
        bookmarkRepository.save(findBookmark);
        findPost.plusBookmarkCnt();
    }

    /**
     * 북마크 해제
     */
    @Retry
    @Transactional
    @Override
    public void cancleBookmark(long userId, long postId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new CNotFoundException("회원: " + userId));
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new CNotFoundException("게시글: " + postId));
        Bookmark findBookmark = bookmarkRepository.findByPostAndUser(findPost, findUser).orElseThrow(() -> new CBadRequestException("잘못된 요청입니다."));
        findBookmark.changeStatus();
        findPost.minusBookmarkCnt();
    }

    private void checkWriter(User user, Post post) {
        if (!post.getUser().getId().equals(user.getId())) {
            throw new CUnAuthorizationException();
        }
    }
}