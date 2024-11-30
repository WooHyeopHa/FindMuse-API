package com.whh.findmuseapi.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateRequest {

    @NotNull(message = "사용자 ID를 입력해 주세요.")
    private Long userId;

    @NotBlank(message = "제목을 입력해 주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해 주세요.")
    private String content;

    @NotBlank(message = "장소를 입력해 주세요.")
    private String place;

    @NotNull(message = "마감일을 입력해 주세요.")
    private LocalDate endDate;

    @NotNull(message = "초대 인원을 입력해 주세요.")
    private int inviteCount;

    @NotBlank(message = "선호 연령을 입력해 주세요.")
    private String ages;

    @NotNull(message = "문화 예술 ID을 입력해 주세요.")
    private Long artId;

    @NotBlank(message = "태그를 입력해 주세요.")
    private List<String> tagList;
}
