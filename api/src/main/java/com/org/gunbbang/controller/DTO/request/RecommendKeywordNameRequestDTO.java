package com.org.gunbbang.controller.DTO.request;

import com.org.gunbbang.RecommendKeyword;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class RecommendKeywordNameRequestDTO {
    @NotNull
    RecommendKeyword keywordName;
}
