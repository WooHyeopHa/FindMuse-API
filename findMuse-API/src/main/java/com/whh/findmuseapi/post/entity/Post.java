package com.whh.findmuseapi.post.entity;

import com.whh.findmuseapi.art.entity.Art;
import com.whh.findmuseapi.common.constant.Infos;
import com.whh.findmuseapi.common.constant.Infos.Ages;
import com.whh.findmuseapi.post.dto.request.PostCreateRequest;
import com.whh.findmuseapi.post.dto.request.PostUpdateRequest;
import com.whh.findmuseapi.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private String place;
    @NotNull
    private LocalDate createDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    private int inviteCount;
    @NotNull
    private int viewCount;
    private int bookmarkCnt;

    @Enumerated(EnumType.STRING)
    private Ages ages; //선호 연령

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "art_id")
    private Art art;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Volunteer> volunteeredList = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<PostTag> tagList = new ArrayList<>();

    public void viewCountPlusOne(){
        this.viewCount = Math.addExact(1, this.viewCount);
    }

    public void updatePost(PostUpdateRequest updateRequest,Art newArt) {
        this.title = updateRequest.getTitle();
        this.content = updateRequest.getContent();
        this.place = updateRequest.getPlace();
        this.endDate = updateRequest.getEndDate();
        this.inviteCount = updateRequest.getInviteCount();
        this.ages = Infos.Ages.valueOf(updateRequest.getAges());
        this.art = newArt;
        this.getTagList().clear();
    }

    public static Post toEntity(PostCreateRequest createRequest,Art art,User user) {
        Post newPost = Post.builder()
                .title(createRequest.getTitle())
                .content(createRequest.getContent())
                .place(createRequest.getPlace())
                .endDate(createRequest.getEndDate())
                .inviteCount(createRequest.getInviteCount())
                .ages(Ages.valueOf(createRequest.getAges()))
                .build();
        newPost.setRelation(user, art);
        return newPost;
    }

    @Builder
    private Post(String title, String content, String place, LocalDate endDate, int inviteCount, Ages ages) {
        this.title = title;
        this.content = content;
        this.place = place;
        this.createDate = LocalDate.now();
        this.endDate = endDate;
        this.inviteCount = inviteCount;
        this.viewCount = 0;
        this.bookmarkCnt = 0;
        this.ages = ages;
    }

    private void setRelation(User user, Art art) {
        this.user = user;
        this.art = art;
        user.getPosts().add(this);
    }
}
