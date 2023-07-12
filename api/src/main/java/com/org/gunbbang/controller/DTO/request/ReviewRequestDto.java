package com.org.gunbbang.controller.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewRequestDto {
    Boolean isLike;
    List<KeywordNameRequestDto> keywordList;
    String reviewText;
}
