package com.whh.findmuseapi.user.dto.response;

import com.whh.findmuseapi.common.constant.Infos;
import com.whh.findmuseapi.user.entity.User;
import lombok.Builder;

@Builder
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
        boolean isOnboardingFinished,
        String refreshToken
) {
    public static UserInfoResponse toUserInfoResponse(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .accountId(user.getAccountId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .birthYear(user.getBirthYear())
                .profileImageUrl(user.getProfileImageUrl())
                .gender(user.getGender())
                .location(user.getLocation())
                .comment(user.getComment())
                .artCount(user.getArtCount())
                .findMuseCount(user.getFindMuseCount())
                .showStatus(user.isShowStatus())
                .alarmStatus(user.isAlarmStatus())
                .activateStatus(user.isActivateStatus())
                .loginType(user.getLoginType())
                .isOnboardingFinished(user.isOnboardingFinished())
                .refreshToken(user.getRefreshToken())
                .build();
    }
}

