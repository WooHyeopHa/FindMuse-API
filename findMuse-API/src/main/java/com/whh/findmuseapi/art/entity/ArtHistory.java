package com.whh.findmuseapi.art.entity;

import com.whh.findmuseapi.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "art_history_id")
    private Long id;
    private float star;
    private boolean activeStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "art_id")
    private Art art;

    public ArtHistory(User user, Art art, float star) {
        this.user = user;
        this.art = art;
        this.star = star;
        this.activeStatus = true;
        user.getHistories().add(this);
    }
}
