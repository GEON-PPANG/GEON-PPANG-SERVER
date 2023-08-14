package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.common.AuthType;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class MemberSignUpResponseDTO {
  private AuthType type;
  private String email;
}
