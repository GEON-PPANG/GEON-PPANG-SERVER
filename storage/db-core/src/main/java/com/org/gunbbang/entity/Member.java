package com.org.gunbbang.entity;

import com.org.gunbbang.PlatformType;
import com.org.gunbbang.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PlatformType platform_type;

    @NotNull
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    private String mainPurpose; // 테이블로 뺄까 고민

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bread_type_id")
    @NotNull
    private BreadType breadTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nutrient_type_id")
    @NotNull
    private NutrientType nutrientTypeId;

}
