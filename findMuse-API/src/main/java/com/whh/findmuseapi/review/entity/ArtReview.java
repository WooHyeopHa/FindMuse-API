package com.whh.findmuseapi.review.entity;

import com.whh.findmuseapi.art.entity.Art;
import com.whh.findmuseapi.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "art_review_id")
    private Long id;
    private String content;
    private String star;
    private LocalDate createDate;
    private int likeCount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "art_id")
    private Art art;

    public ArtReview(String content, String star, User user, Art art) {
        this.content = content;
        this.star = star;
        this.createDate = LocalDate.now();
        this.likeCount = 0;
        this.createDate = LocalDate.now();
        updateRelation(user, art);
    }

    private void updateRelation(User user, Art art) {
        this.user = user;
        this.art = art;
        user.getArtReviews().add(this);
        art.getArtReviews().add(this);
    }

    public void updateReview(String newContent) {
        this.content = newContent;
    }

    public void plusLikeCount() {
        //TODO : 동시성
        this.likeCount++;
    }

    public void minusLikeCount() {
        //TODO : 동시성
        this.likeCount--;
    }
}
