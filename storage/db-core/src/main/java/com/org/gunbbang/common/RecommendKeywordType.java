package com.org.gunbbang.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum RecommendKeywordType {
    DELICIOUS("맛있어요"),
    KIND("친절해요"),
    SPECIAL_MENU("특별한 메뉴"),
    ZERO_WASTE("제로 웨이스트");

    private final String name;
}
