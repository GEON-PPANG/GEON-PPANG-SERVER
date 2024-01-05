package com.org.gunbbang.controller.DTO.request;

import com.org.gunbbang.MainPurpose;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberTypesRequestDTO {
  @NotNull private MainPurpose mainPurpose;
  @NotNull private List<Long> breadTypeList;
  @NotNull private List<Long> nutrientTypeList;

  @Override
  public String toString() {
    return "MemberTypesRequestDTO{"
        + "mainPurpose="
        + mainPurpose
        + ", breadType="
        + breadTypeList
        + ", nutrientType="
        + breadTypeList
        + '}';
  }
}
