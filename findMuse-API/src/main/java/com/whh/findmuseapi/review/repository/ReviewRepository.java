package com.whh.findmuseapi.review.repository;

import com.whh.findmuseapi.art.entity.Art;
import com.whh.findmuseapi.review.entity.ArtReview;
import com.whh.findmuseapi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ArtReview, Long> {

    boolean existsByUserAndArt(User user, Art art);
    ArtReview findByUserAndArt(User user, Art art);

    @Query("select ar from ArtReview ar left join ar.reviewLikes arl on ar.id = arl.artReview.id where ar.art.id = :artId and arl.user.id = :userId order by ar.createDate desc")
    List<ArtReview> findAllByArtOrderByCreateDateDesc(Long artId, Long userId);

//    @Query("select ar from ArtReview ar left join ar.reviewLikes arl on ar.id = arl.artReview.id where ar.art.id = :artId and arl.user.id = :userId order by ar.likeCount desc")
//    List<ArtReview> findAllByArtOrderByLikeCountDesc(Long artId, Long userId);
}
