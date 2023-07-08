package com.org.gunbbang.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BreadType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bread_type_id")
    private Long breadTypeId;

    /**
     * 글루텐프리,
     * 비건빵,
     * 넛프리,
     * 저당 및 무설탕
     */
    @NotNull
    private String breadTypeName;

    @NotNull
    private boolean isGlutenFree; // 글루텐프리

    @NotNull
    private boolean isVegan; // 비건빵

    @NotNull
    private boolean isNutFree; // 넛프리

    @NotNull
    private boolean isSugarFree; // 저당 및 무설탕

}
