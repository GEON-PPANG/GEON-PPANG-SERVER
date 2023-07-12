package com.org.gunbbang.controller.DTO.response;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ReviewListResponseDto {
    @NotNull
    Float tastePercent;
    @NotNull
    Float specialPercent;
    @NotNull
    Float kindPercent;
    List<ReviewResponseDto> reviewList;
}
