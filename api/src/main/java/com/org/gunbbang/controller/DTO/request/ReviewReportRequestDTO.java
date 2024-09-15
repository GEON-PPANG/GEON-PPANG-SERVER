package com.org.gunbbang.controller.DTO.request;

import com.org.gunbbang.ReportCategory;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReviewReportRequestDTO {
  @NotNull private String content;

  @NotNull private ReportCategory reportCategory;

  @Override
  public String toString() {
    return "ReviewReportRequestDTO{"
        + "content='"
        + content
        + '\''
        + ", reportCategory="
        + reportCategory
        + '}';
  }
}
