package com.whh.findmuseapi.review.repository;

import com.whh.findmuseapi.art.entity.Art;
import com.whh.findmuseapi.review.entity.ArtReview;
import com.whh.findmuseapi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ArtReview, Long> {

    Optional<List<ArtReview>> findAllByArtOrderByCreateDateDesc(Art art);
    Optional<List<ArtReview>> findAllByArtOrderByLikeCountDesc(Art art);
    boolean existsByUserAndArt(User user, Art art);
    ArtReview findByUserAndArt(User user, Art art);
}
