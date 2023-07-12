package com.org.gunbbang.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BakeryCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bakeryCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bakery_id")
    @NotNull
    private Bakery bakery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @NotNull
    private Category category;

}
