package com.whh.findmuseapi.review.repository;

import com.whh.findmuseapi.art.entity.Art;
import com.whh.findmuseapi.review.entity.ArtReview;
import com.whh.findmuseapi.user.entity.User;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ArtReview, Long> {

    boolean existsByUserAndArt(User user, Art art);
    ArtReview findByUserAndArt(User user, Art art);

    @Query("select ar, arl from ArtReview ar left join ar.reviewLikes arl on arl.user.id = :userId where ar.art.id = :artId order by ar.createDate desc")
    Optional<List<Tuple>> findAllReviewByUserCreateDateDesc(Long artId, Long userId);

    @Query("select ar, arl from ArtReview ar left join ar.reviewLikes arl on arl.user.id = :userId where ar.art.id = :artId")
    Optional<List<Tuple>> findAllReviewByUser(Long artId, Long userId);
}
