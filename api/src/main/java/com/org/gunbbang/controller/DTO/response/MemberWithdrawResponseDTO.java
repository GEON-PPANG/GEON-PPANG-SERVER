package com.org.gunbbang.controller.DTO.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class MemberWithdrawResponseDTO {
  private Long memberId;
}
