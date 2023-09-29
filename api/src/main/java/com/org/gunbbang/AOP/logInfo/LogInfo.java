package com.org.gunbbang.AOP.logInfo;

import java.util.Map;
import lombok.Getter;

@Getter
public class LogInfo {
  private Long memberId;
  private String url;
  private String name;
  private String method;
  private Map<String, Object> parameters;
  private Map<String, Object> body;
  private String exception = null;
  private String exceptionSimpleName = null;

  public LogInfo(
      Long memberId,
      String url,
      String name,
      String method,
      Map<String, Object> parameters,
      Map<String, Object> body) {
    this.memberId = memberId;
    this.url = url;
    this.name = name;
    this.method = method;
    this.parameters = parameters;
    this.body = body;
  }

  public LogInfo() {}

  public void setException(String exception) {
    this.exception = exception;
  }

  public void setExceptionSimpleName(String exceptionSimpleName) {
    this.exceptionSimpleName = exceptionSimpleName;
  }
}
