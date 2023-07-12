package com.org.gunbbang.controller.DTO.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecommendKeywordResponseDto {
    @NotNull
    Long recommendKeywordId;
    @NotNull
    String recommendKeywordName;

    @Builder
    public RecommendKeywordResponseDto(Long recommendKeywordId, String recommendKeywordName) {
        this.recommendKeywordId = recommendKeywordId;
        this.recommendKeywordName = recommendKeywordName;
    }
}
