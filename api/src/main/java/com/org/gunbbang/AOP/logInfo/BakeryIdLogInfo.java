package com.org.gunbbang.AOP.logInfo;

import lombok.Getter;

@Getter
public class BakeryIdLogInfo extends LogInfo {
    private Long bakeryId;

    public BakeryIdLogInfo(
            String url, String name, String method, String header, String parameters, String body, String ipAddress, Long bakeryId
    ) {
        super(url, name, method, header, parameters, body, ipAddress);
        this.bakeryId = bakeryId;
    }
}
