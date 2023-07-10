package com.org.gunbbang.controller.DTO;

import com.org.gunbbang.PlatformType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class MemberSignUpRequestDTO {
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String nickName;
    @NotNull
    private String mainPurpose;
    @NotNull
    private PlatformType platformType;
    /**
     * 이거 추가되어야 함
     * 클라에서는 아래처럼 보내줄거임
     *     isGlutenFree: true
     *     isVegan: false
     *     isNutFree: false
     *     isSugarFree: ture
     *
     * 그럼 보내준 이 응답을 바탕으로 repository에서 조회
     * -> 조회해서 가져온 id로 저장
     */
}
