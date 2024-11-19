package com.whh.findmuseapi.art.service;

import com.whh.findmuseapi.art.dto.*;

import java.util.List;


public interface ArtService {

    ArtOneResponse getArtInfoOne(Long artId, Long userId);

    ArtListResponse getArtByCondition(Long userId, String date, List<String> genre, String sort);

    void markLike(ArtLikeRequest artLikeRequest);

    ArtHomeResponse getArtByHome(Long userId);

    MapResponse getMapInfo();

    ArtTicketResponse getTicketInfo(long artId);
}
