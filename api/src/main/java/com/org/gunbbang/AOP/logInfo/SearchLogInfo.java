package com.org.gunbbang.AOP.logInfo;

import lombok.Getter;

@Getter
public class SearchLogInfo extends LogInfo {
  private String searchKeyword;

  public SearchLogInfo(
      String url,
      String name,
      String method,
      String header,
      String parameters,
      String body,
      String ipAddress,
      String searchKeyword) {
    super(url, name, method, header, parameters, body, ipAddress);
    this.searchKeyword = searchKeyword;
  }
}
