package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.MainPurpose;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class MemberTypeResponseDTO {
  @NotNull private Long memberId;
  @NotNull private MainPurpose mainPurpose;
  @NotNull private String nickname;
  @NotNull private List<Long> breadTypeList;
  @NotNull private List<Long> nutrientTypeList;
}
