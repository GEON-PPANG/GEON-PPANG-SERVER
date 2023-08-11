package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.controller.DTO.response.BaseDTO.BaseBakeryResponseDTO;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@SuperBuilder
public class BakeryDetailResponseDTO extends BaseBakeryResponseDTO {
  private Long reviewCount;
  private BreadTypeResponseDTO breadType;
  private String mapUrl;
  private String homepage;
  private String address;
  private String openingTime;
  private String closedDay;
  private String phoneNumber;
  private List<MenuResponseDTO> menuList;
  private boolean isBookMarked;
  private long bookMarkCount;

  public boolean getIsBookMarked() {
    return isBookMarked;
  }
}
