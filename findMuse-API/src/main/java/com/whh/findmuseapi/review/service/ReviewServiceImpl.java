package com.whh.findmuseapi.review.service;

import com.whh.findmuseapi.art.entity.Art;
import com.whh.findmuseapi.art.entity.ArtHistory;
import com.whh.findmuseapi.art.repository.ArtHistoryRepository;
import com.whh.findmuseapi.art.repository.ArtRepository;
import com.whh.findmuseapi.review.dto.*;
import com.whh.findmuseapi.review.entity.ArtReview;
import com.whh.findmuseapi.review.entity.ArtReviewLike;
import com.whh.findmuseapi.review.repository.ReviewLikeRepository;
import com.whh.findmuseapi.review.repository.ReviewRepository;
import com.whh.findmuseapi.user.entity.User;
import com.whh.findmuseapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService{

    private final ArtRepository artRepository;
    private final ArtHistoryRepository artHistoryRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;

    /**
     * 모든 리뷰 조회
     */
    @Override
    public AllReviewResponse getArtReview(long artId, long userId, String creteria) {
        Art findArt = artRepository.findById(artId).orElseThrow();
        List<ArtReview> result;
        if(creteria.equals("like")) {
            result = reviewRepository.findAllByArtOrderByLikeCountDesc(artId, userId).orElse(new ArrayList<>());
        }
        else{
            result = reviewRepository.findAllByArtOrderByCreateDateDesc(artId, userId).orElse(new ArrayList<>());
        }
        return AllReviewResponse.toDto(findArt.getStar(), result);
    }

    /**
     * 리뷰 작성
     */
    @Override
    @Transactional
    public void createReview(ReviewRequest reviewRequest) {
        User findUser = userRepository.findById(reviewRequest.getUserId()).orElseThrow();
        Art findArt = artRepository.findById(reviewRequest.getArtId()).orElseThrow();
        ArtReview artReview = new ArtReview(reviewRequest.getContent(), findUser, findArt);
        reviewRepository.save(artReview);
        //TODO : 예외처리
    }

    /**
     * 리뷰 수정
     */
    @Override
    @Transactional
    public void updateReview(ReviewUpdateRequest reviewUpdateRequest) {
        //TODO: 예외처리
        ArtReview findReview = reviewRepository.findById(reviewUpdateRequest.getReviewId()).orElseThrow();

        if (findReview.getUser().getId() == reviewUpdateRequest.getUserId()) {
            findReview.updateReview(reviewUpdateRequest.getContent());
            return;
        }
        //TODO : 에외처리
    }

    /**
     * 리뷰 삭제
     */
    @Override
    public void deleteReview(long userId, long reviewId) {
        //TODO: 예외처리
        ArtReview findReview = reviewRepository.findById(reviewId).orElseThrow();

        if (findReview.getUser().getId() == userId) {
            reviewRepository.delete(findReview);
        }
        //TODO : 에외처리
    }

    /**
     * 리뷰 공감하기
     */
    @Override
    @Transactional
    public boolean likeReview(ReviewLikeRequest reviewLikeRequest) {
        User findUser = userRepository.findById(reviewLikeRequest.getUserId()).orElseThrow();
        ArtReview artReview = reviewRepository.findById(reviewLikeRequest.getReviewId()).orElseThrow();

        reviewLikeRepository.save(new ArtReviewLike(findUser, artReview));
        return true;
    }

    /**
     * 공감한 리뷰 취소
     */
    @Override
    @Transactional
    public void likeReviewCancle(long reviewId, long userId) {
        User findUser = userRepository.findById(userId).orElseThrow();
        ArtReview artReview = reviewRepository.findById(reviewId).orElseThrow();
        reviewLikeRepository.deleteArtReviewLikeByUserAndArtReview(findUser, artReview);
    }

    /**
     * 문화예술 별점 매기기
     */
    @Override
    @Transactional
    public void giveReviewStar(ReviewStarRequest reviewStarRequest) {
        User findUser = userRepository.findById(reviewStarRequest.getUserId()).orElseThrow();
        Art findArt = artRepository.findById(reviewStarRequest.getArtId()).orElseThrow();
        //이미 관람한 경우
        if (reviewStarRequest.isStared()) {
            ArtHistory findHistory = artHistoryRepository.findByUserAndArt(findUser, findArt);
            findArt.updateStar(findHistory.getStar(), reviewStarRequest.getStar());
        }
        // 처음 관람인 경우
        else {
            findArt.plusViewAndCalStar(reviewStarRequest.getStar());
            artHistoryRepository.save(new ArtHistory(findUser, findArt, reviewStarRequest.getStar()));
            ArtReview findReview = reviewRepository.findByUserAndArt(findUser, findArt);
            findReview.updateStar(String.valueOf(reviewStarRequest.getStar()));
        }
    }
}
