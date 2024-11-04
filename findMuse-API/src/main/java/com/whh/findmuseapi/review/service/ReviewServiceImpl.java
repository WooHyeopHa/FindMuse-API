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

import java.util.List;

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
    public AllReviewResponse getArtReview(Long artId) {
        Art findArt = artRepository.findById(artId).orElseThrow();
        List<ArtReview> artReviews = findArt.getArtReviews();
        return AllReviewResponse.toDto(artReviews);
    }

    /**
     * 리뷰 작성
     */
    @Override
    @Transactional
    public void createReview(ReviewRequest reviewRequest) {
        User findUser = userRepository.findById(reviewRequest.getUserId()).orElseThrow();
        Art findArt = artRepository.findById(reviewRequest.getArtId()).orElseThrow();
        //TODO : 저장되는지 확인하기
        ArtReview artReview = new ArtReview(reviewRequest.getContent(), String.valueOf(reviewRequest.getStar()), findUser, findArt);
        artRepository.save(findArt);
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
    public void deleteReview(Long userId, Long reviewId) {
        //TODO: 예외처리
        ArtReview findReview = reviewRepository.findById(reviewId).orElseThrow();

        if (findReview.getUser().getId() == userId) {
            reviewRepository.delete(findReview);
            return;
        }
        //TODO : 에외처리
    }

    /**
     * 리뷰 공감하기
     */
    @Override
    @Transactional
    public boolean likeReview(ReviewLikeRequest reviewLikeRequest) {
        //TODO : 1. 이미 공감한 리뷰인지 / 2. 동시성 제어
        User findUser = userRepository.findById(reviewLikeRequest.getUserId()).orElseThrow();
        ArtReview artReview = reviewRepository.findById(reviewLikeRequest.getReviewId()).orElseThrow();

        //이미 공감한 댓글인 경우
        if (reviewLikeRepository.existsByUserAndArtReview(findUser, artReview)) {
            return false;
        }
        artReview.plusLikeCount();
        reviewLikeRepository.save(new ArtReviewLike(findUser, artReview));
        return true;
    }

    /**
     * 공감한 리뷰 취소
     */
    @Override
    @Transactional
    public void likeReviewCancle(Long reviewId, Long userId) {
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
        //이미 관렴한 경우
        if (reviewStarRequest.isViewed()) {
            ArtHistory findHostory = artHistoryRepository.findByUserAndArt(findUser, findArt);
            findArt.updateStar(findHostory.getStar(), reviewStarRequest.getStar());
        }
        // 처음 관람인 경우
        else {
            findArt.plusViewAndCalStar(reviewStarRequest.getStar());
            artHistoryRepository.save(new ArtHistory(findUser, findArt, reviewStarRequest.getStar()));
        }
    }
}
