package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.MainPurpose;
import com.org.gunbbang.entity.BreadType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberDetailResponseDto {
    private String memberNickname;
    private MainPurpose mainPurpose;
    private BreadTypeResponseDto breadType;

    @Builder
    public MemberDetailResponseDto(String memberNickname, MainPurpose mainPurpose, BreadTypeResponseDto breadType) {
        this.memberNickname = memberNickname;
        this.mainPurpose = mainPurpose;
        this.breadType = breadType;
    }
}
