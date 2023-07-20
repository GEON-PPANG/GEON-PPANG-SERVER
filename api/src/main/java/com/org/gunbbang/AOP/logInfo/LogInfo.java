package com.org.gunbbang.AOP.logInfo;

import lombok.Getter;

@Getter
public class LogInfo {
    private String url;
    private String name;
    private String method;
    private String header;
    private String parameters;
    private String body;
    private String ipAddress;
//    private Long memberId;
    private String exception = null;
    private String exceptionSimpleName = null;

    public LogInfo(
            String url, String name, String method, String header, String parameters, String body, String ipAddress
    ) {
        this.url = url;
        this.name = name;
        this.method = method;
        this.header = header;
        this.parameters = parameters;
        this.body = body;
        this.ipAddress = ipAddress;
    }

    public LogInfo() {
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public void setExceptionSimpleName(String exceptionSimpleName) {
        this.exceptionSimpleName = exceptionSimpleName;
    }
}
