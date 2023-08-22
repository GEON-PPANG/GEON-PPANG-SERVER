package com.org.gunbbang.controller.DTO.response;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ReviewReportResponseDTO {
  private final Long reviewReportId;
}
