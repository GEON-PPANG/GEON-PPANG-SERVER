package com.org.gunbbang.controller.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewRequestDto {
    @NotNull
    private Boolean isLike;
    private List<KeywordNameRequestDto> keywordList;
    @NotNull
    private String reviewText;
}
