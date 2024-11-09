package com.whh.findmuseapi.art.repository;

import com.whh.findmuseapi.art.entity.Art;
import com.whh.findmuseapi.art.entity.ArtHistory;
import com.whh.findmuseapi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtHistoryRepository extends JpaRepository<ArtHistory, Long> {
    List<ArtHistory> findByUserId(Long userId);
    ArtHistory findByUserAndArt(User user, Art art);
    boolean existsByUserAndArt(User user, Art art);
}
