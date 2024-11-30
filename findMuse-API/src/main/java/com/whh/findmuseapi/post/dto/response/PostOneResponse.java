package com.whh.findmuseapi.post.dto.response;

import com.whh.findmuseapi.post.entity.Post;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * class: PostReadResponse.
 * 모집글 상세 조회 dto 입니다.
 * @author devminseo
 * @version 8/21/24
 */
@Getter
@Builder
public class PostOneResponse {
    private Long postId;
    private String title;
    private String content;
    private String place;
    private LocalDate endDate;
    private int dDay;
    private int inviteCnt;
    private int invitedCnt;
    private int viewCnt;
    private int bookmarkCnt;
    private String ages; //선호 연령
    private String artName;
    private List<String> tagList;
    private Long writterId;
    private String writterName;
    private boolean isWriter;


    public static PostOneResponse toDto(Post post, int invitedCount, boolean isWriter) {
        return PostOneResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .place(post.getPlace())
                .endDate(post.getEndDate())
                .dDay(calculateDDay(post.getEndDate()))
                .inviteCnt(post.getInviteCount())
                .invitedCnt(invitedCount)
                .viewCnt(post.getViewCount())
                .bookmarkCnt(post.getBookmarkCnt())
                .ages(post.getAges().name())
                .artName(post.getArt().getTitle())
                .tagList(post.getTagList().stream()
                        .map(tag -> tag.getTag().getName())
                        .toList())
                .writterId(post.getUser().getId())
                .writterName(post.getUser().getNickname())
                .isWriter(isWriter)
                .build();
    }

    private static int calculateDDay(LocalDate endDate) {
        LocalDate now = LocalDate.now();
        return (int) ChronoUnit.DAYS.between(now, endDate);
    }
}
