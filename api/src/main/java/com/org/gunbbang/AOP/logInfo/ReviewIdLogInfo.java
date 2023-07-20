package com.org.gunbbang.AOP.logInfo;

import lombok.Getter;

@Getter
public class ReviewIdLogInfo extends LogInfo {
    private Long reviewId;

    public ReviewIdLogInfo(
            String url, String name, String method, String header, String parameters, String body, String ipAddress, Long reviewId
    ) {
        super(url, name, method, header, parameters, body, ipAddress);
        this.reviewId = reviewId;
    }
}
