package com.org.gunbbang.controller.DTO.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookMarkRequestDTO {
  @NotNull private boolean isAddingBookMark;

  public boolean getIsAddingBookMark() {
    return isAddingBookMark;
  }

  @Override
  public String toString() {
    return "BookMarkRequestDTO{" + "isAddingBookMark=" + isAddingBookMark + '}';
  }
}
