package com.whh.findmuseapi.common.constant;

import com.whh.findmuseapi.common.exception.CBadRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

public class Infos {
    @Getter
    @RequiredArgsConstructor
    public enum Gender {
        MEN("남성"),
        WOMEN("여성");

        private final String description;

        public static Gender convertStringToGender(String value) {
            return Arrays.stream(Gender.values())
                    .filter(gender -> gender.description.equals(value))
                    .findFirst()
                    .orElseThrow(() -> new CBadRequestException("Invalid ReviewSortType: " + value));
        }
    }

    @RequiredArgsConstructor
    public enum LoginType {
        APPLE("애플 로그인");

        private final String description;
    }

    @RequiredArgsConstructor
    public enum Ages {
        ALL("All"),
        TEENAGER("10대"),
        TWENTIES("20대"),
        THIRTIES("30대"),
        FOURTIES("40대"),
        REST("50+");

        private final String description;
    }

    @RequiredArgsConstructor
    public enum InvieteStatus {
        ACCESS("승인됨"),
        DENY("거절됨"),
        Wait("대기중");

        private final String description;
    }

    public enum Rating {

    }

    @RequiredArgsConstructor
    @Getter
    public enum Genre {
        MUSICAL_DRAMA("뮤지컬/연극"),
        EXHIBITION("전시회"),
        DANCE_CLASSIC("무용/클래식"),
        CONCERT("콘서트");

        private final String description;
        public static Genre convertStringToGenre(String value){
            return Arrays.stream(Genre.values())
                    .filter(genre -> genre.description.equals(value))
                    .findFirst()
                    .orElseThrow(() -> new CBadRequestException("Invalid ReviewSortType: " + value));
        }

    }

    @Getter
    @RequiredArgsConstructor
    public enum ReviewSortType {
        LATEST("최신순"),
        POPULAR("인기순");

        private final String description;

        public static ReviewSortType convertStringToSortType(String value) {
            return Arrays.stream(ReviewSortType.values())
                    .filter(sort -> sort.description.equals(value))
                    .findFirst()
                    .orElseThrow(()-> new CBadRequestException("Invalid ReviewSortType: " + value));
        }
    }

    @RequiredArgsConstructor
    public enum AlarmType {
        ACTIVITY("활동"),
        GREETING("일정");

        private final String info;
    }
    
    @Getter
    @RequiredArgsConstructor
    public enum Role {
        GUEST("GUEST", "게스트"),          // 추가 정보 입력 전
        USER("USER", "일반 사용자");        // 추가 정보 입력 완
        
        private final String key;
        private final String name;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ResourceUrl {
        PROFILE_IMAGE("/user/profile/");

        private final String url;
    }

}