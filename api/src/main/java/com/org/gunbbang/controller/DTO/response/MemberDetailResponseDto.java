package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.entity.BreadType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberDetailResponseDto {
    private String memberNickname;
    private String mainPurpose;
    private BreadTypeResponseDto breadType;

    @Builder
    public MemberDetailResponseDto(String memberNickname, String mainPurpose, BreadTypeResponseDto breadType) {
        this.memberNickname = memberNickname;
        this.mainPurpose = mainPurpose;
        this.breadType = breadType;
    }
}
