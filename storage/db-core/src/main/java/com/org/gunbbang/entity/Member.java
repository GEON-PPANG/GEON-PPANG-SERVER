package com.org.gunbbang.entity;

import com.org.gunbbang.MainPurpose;
import com.org.gunbbang.PlatformType;
import com.org.gunbbang.Role;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseEntity{
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
    private PlatformType platformType;

    @NotNull
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MainPurpose mainPurpose; // 테이블로 뺄까 고민

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bread_type_id")
    @NotNull
    private BreadType breadType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nutrient_type_id")
    @NotNull
    private NutrientType nutrientType;

    private String refreshToken;

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    // TODO: 이거 모듈 관련해서 의존성 추가하는 문제 생각해봐야함
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
