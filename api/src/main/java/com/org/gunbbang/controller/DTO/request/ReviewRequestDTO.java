package com.org.gunbbang.controller.DTO.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequestDTO {
  @NotNull private Boolean isLike;
  private List<RecommendKeywordNameRequestDTO> keywordList;
  @NotNull private String reviewText;

  @Override
  public String toString() {
    return "ReviewRequestDTO{"
        + "isLike="
        + isLike
        + ", keywordList="
        + keywordList
        + ", reviewText='"
        + reviewText
        + '\''
        + '}';
  }
}
