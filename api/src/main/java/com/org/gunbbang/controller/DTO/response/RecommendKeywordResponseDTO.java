package com.org.gunbbang.controller.DTO.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecommendKeywordResponseDTO {
    @NotNull
    Long recommendKeywordId;
    @NotNull
    String recommendKeywordName;

    @Builder
    public RecommendKeywordResponseDTO(Long recommendKeywordId, String recommendKeywordName) {
        this.recommendKeywordId = recommendKeywordId;
        this.recommendKeywordName = recommendKeywordName;
    }
}
