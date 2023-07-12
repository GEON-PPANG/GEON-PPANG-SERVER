package com.org.gunbbang;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MainPurpose {
    HEALTH("건강/체질"),
    DIET("맛/디이어트"),
    VEGAN("비건/채식지향")
    ;

    private String desc;
}
