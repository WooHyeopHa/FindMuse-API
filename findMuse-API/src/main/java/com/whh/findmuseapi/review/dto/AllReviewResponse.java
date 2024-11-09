package com.whh.findmuseapi.review.dto;

import com.whh.findmuseapi.review.entity.ArtReview;
import com.whh.findmuseapi.review.entity.ArtReviewLike;
import com.whh.findmuseapi.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Builder
@Getter
public class AllReviewResponse {

    private int reviewCnt;
    private float totalStar;
    private List<SingleReviewResponse> reviewList;


    public static AllReviewResponse toDto(float star, List<Object[]> reviews) {
        return AllReviewResponse.builder()
                .reviewCnt(reviews.size())
                .totalStar(star)
                .reviewList(SingleReviewResponse.toDto(reviews)).build();
    }

    @Builder
    @Getter
    private static class SingleReviewResponse {
        private String name;
        private String userPhoto;
        private String star;
        private String content;
        private String date;
        private boolean isThumbed;
        private int thumbsUpCnt;

        private static List<SingleReviewResponse> toDto(List<Object[]> reviews) {
            return reviews.stream()
                    .map(r -> {
                        ArtReview review = (ArtReview) r[0];
                        ArtReviewLike reviewLike = (ArtReviewLike) r[1];
                        User writeUser = review.getUser();
                        return SingleReviewResponse.builder()
                                .name(writeUser.getNickname())
                                .userPhoto(writeUser.getProfileImageUrl())
                                .star(review.getStar())
                                .content(review.getContent())
                                .date(review.getCreateDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
                                .isThumbed(reviewLike.getUser() != null)
                                .thumbsUpCnt(review.getLikeCount()).build();
                    }).toList();
        }
    }
}
