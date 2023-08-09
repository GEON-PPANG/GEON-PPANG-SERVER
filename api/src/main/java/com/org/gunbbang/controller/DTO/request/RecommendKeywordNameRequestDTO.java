package com.org.gunbbang.controller.DTO.request;

import com.org.gunbbang.RecommendKeyword;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecommendKeywordNameRequestDTO {
  @NotNull RecommendKeyword keywordName;

  @Override
  public String toString() {
    return "RecommendKeywordNameRequestDTO{" + "keywordName='" + keywordName + '\'' + '}';
  }
}
