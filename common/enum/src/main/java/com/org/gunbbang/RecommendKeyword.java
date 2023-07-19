package com.org.gunbbang;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecommendKeyword {
    TASTE("맛있어요"),
    KIND("친절해요"),
    SPECIAL("특별한 메뉴"),
    ZERO_WASTE("제로웨이스트")
    ;

    private String message;
}
