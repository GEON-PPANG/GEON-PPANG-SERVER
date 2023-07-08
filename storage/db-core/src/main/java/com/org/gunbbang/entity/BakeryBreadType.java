package com.org.gunbbang.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

//@Entity
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BakeryBreadType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bakeryBreadTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bakery_id")
    @NotNull
    private Bakery bakeryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bread_type_id")
    @NotNull
    private BreadType breadTypeId;
}
