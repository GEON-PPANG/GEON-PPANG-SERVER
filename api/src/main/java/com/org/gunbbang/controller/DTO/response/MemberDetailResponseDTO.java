package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.MainPurpose;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberDetailResponseDTO {
  private String memberNickname;
  private MainPurpose mainPurpose;
  private List<BreadTypeResponseDTO> breadType;

  @Builder
  public MemberDetailResponseDTO(
      String memberNickname, MainPurpose mainPurpose, List<BreadTypeResponseDTO> breadType) {
    this.memberNickname = memberNickname;
    this.mainPurpose = mainPurpose;
    this.breadType = breadType;
  }
}
