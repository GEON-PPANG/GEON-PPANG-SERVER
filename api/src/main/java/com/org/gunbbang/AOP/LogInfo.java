package com.org.gunbbang.AOP;

import lombok.Getter;

import java.util.Map;

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
    private String exception;

    public LogInfo(String url, String name, String method, String header, String parameters, String body, String ipAddress) {
        this.url = url;
        this.name = name;
        this.method = method;
        this.header = header;
        this.parameters = parameters;
        this.body = body;
        this.ipAddress = ipAddress;
//        this.memberId = memberId;
    }

//    public LogInfo(String url, String name, String method, Map<String, String> header, String parameters, String body, String ipAddress, Long memberId) {
//    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
