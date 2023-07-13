package com.org.gunbbang.controller.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewRequestDTO {
    @NotNull
    private Boolean isLike;
    private List<RecommendKeywordNameRequestDTO> keywordList;
    @NotNull
    private String reviewText;
}
