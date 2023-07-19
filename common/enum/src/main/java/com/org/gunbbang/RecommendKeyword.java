package com.org.gunbbang;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecommendKeyword {
    DELICIOUS("맛있어요"),
    KIND("친절해요"),
    SPECIAL_MENU("특별한 메뉴"),
    ZERO_WASTE("제로 웨이스트")
    ;

    private String message;
}
