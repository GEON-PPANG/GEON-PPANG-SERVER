package com.org.gunbbang.AOP.logInfo;

import lombok.Getter;

@Getter
public class SignupLogInfo extends LogInfo {

  public SignupLogInfo(
      String url,
      String name,
      String method,
      String header,
      String parameters,
      String body,
      String ipAddress) {
    super(url, name, method, header, parameters, body, ipAddress);
  }
}
