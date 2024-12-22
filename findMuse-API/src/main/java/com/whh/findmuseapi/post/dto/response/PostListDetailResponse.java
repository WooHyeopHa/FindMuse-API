package com.whh.findmuseapi.post.dto.response;

import com.whh.findmuseapi.post.entity.Post;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

/**
 * class: PostListDetailResponse.
 *
 * @author devminseo
 * @version 9/3/24
 */
@Getter
@Builder
public class PostListDetailResponse {
    private Long id;
    private String title;
    private String content;
    private String writerName;
    private LocalDate createDate;
    private boolean isBookmarked;
    //TODO : 참여중인 인원은?
    private int inviteCnt;
    private String poster;

    public static PostListDetailResponse toDto(Post post, boolean isBookmarked) {
        return PostListDetailResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .writerName(post.getUser().getNickname())
                .createDate(post.getCreateDate())
                .isBookmarked(isBookmarked)
                .inviteCnt(post.getInviteCount())
                .poster(post.getArt().getPoster())
                .build();
    }
}
