package com.org.gunbbang.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class RecommendKeywordPercentage {
  private float deliciousPercent;
  private float specialPercent;
  private float kindPercent;
  private float zeroWastePercent;
}
