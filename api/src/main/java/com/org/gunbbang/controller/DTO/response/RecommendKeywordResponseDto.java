package com.org.gunbbang.controller.DTO.response;

import lombok.AccessLevel;
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
}
