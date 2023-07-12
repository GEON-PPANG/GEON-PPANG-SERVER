package com.org.gunbbang.controller.DTO.response;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ReviewResponseDto {
    @NotNull
    Long reviewId;
    @NotNull
    String memberNickname;
    List<RecommendKeywordResponseDto> recommendKeywordList;
    @NotNull
    String reviewText;
    @NotNull
    String createdAt;
}
