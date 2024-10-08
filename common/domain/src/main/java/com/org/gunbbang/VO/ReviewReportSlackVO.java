package com.org.gunbbang.VO;

import com.org.gunbbang.ReportCategory;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class ReviewReportSlackVO {
  private final String bakeryName;
  private final Long reporterId;
  private final Long reviewReportId;
  private final ReportCategory reportCategory;
  private final String reportContent;
  private final String reviewContent;
  private final LocalDateTime reportedAt;
}
