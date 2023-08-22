package com.org.gunbbang;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReportCategory {
  ADVERTISING("홍보 및 신뢰할 수 없는 게시물"),
  HATE("욕설, 음란어, 혐오 발언 사용"),
  COPYRIGHT("명예회손, 저작권 침해"),
  ETC("그 외 다른 문제");
  private String desc;
}
