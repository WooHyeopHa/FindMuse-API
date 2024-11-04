package com.whh.findmuseapi.user.dto.response;

import com.whh.findmuseapi.common.constant.Infos;
import com.whh.findmuseapi.user.entity.User;

public record UserInfoResponse(
        Long id,
        String accountId,
        String email,
        String nickname,
        int birthYear,
        String profileImageUrl,
        Infos.Gender gender,
        String location,
        String comment,
        int artCount,
        int findMuseCount,
        boolean showStatus,
        boolean alarmStatus,
        boolean activateStatus,
        Infos.LoginType loginType,
        String refreshToken
) {
    public static UserInfoResponse toUserInfoResponse(User user) {
        return new UserInfoResponse(
                user.getId(),
                user.getAccountId(),
                user.getEmail(),
                user.getNickname(),
                user.getBirthYear(),
                user.getProfileImageUrl(),
                user.getGender(),
                user.getLocation(),
                user.getComment(),
                user.getArtCount(),
                user.getFindMuseCount(),
                user.isShowStatus(),
                user.isAlarmStatus(),
                user.isActivateStatus(),
                user.getLoginType(),
                user.getRefreshToken()
        );
    }
}

