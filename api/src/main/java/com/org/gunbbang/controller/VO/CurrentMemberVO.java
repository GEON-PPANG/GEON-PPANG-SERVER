package com.org.gunbbang.controller.VO;

import com.org.gunbbang.MainPurpose;
import lombok.Getter;

@Getter
public class CurrentMemberVO {
  private final Long currentMemberId;
  private final String currentEmail;
  private final String currentPassword;
  private final String currentNickname;
  private final MainPurpose currentMainPurpose;

  private CurrentMemberVO(
      Long memberId, String email, String password, String nickname, MainPurpose mainPurpose) {
    currentMemberId = memberId;
    currentEmail = email;
    currentPassword = password;
    currentNickname = nickname;
    currentMainPurpose = mainPurpose;
  }

  public static CurrentMemberVO of(
      Long memberId, String email, String password, String nickname, MainPurpose mainPurpose) {
    return new CurrentMemberVO(memberId, email, password, nickname, mainPurpose);
  }
}
