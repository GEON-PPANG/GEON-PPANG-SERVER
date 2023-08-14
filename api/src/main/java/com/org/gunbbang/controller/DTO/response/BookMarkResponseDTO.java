package com.org.gunbbang.controller.DTO.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class BookMarkResponseDTO {
  private long bookMarkCount;
  private boolean isBookMarked;

  public boolean getIsBookMarked() {
    return isBookMarked;
  }
}
