package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.Role;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class NicknameUpdateResponseDTO {
  private String nickname;
  private Role role;
  private Long memberId;
}
