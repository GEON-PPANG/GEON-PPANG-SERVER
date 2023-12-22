package com.org.gunbbang.auth.security.util;

import com.org.gunbbang.MainPurpose;
import com.org.gunbbang.auth.security.CustomUserDetails;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * SecurityContextHolder로부터 얻은 Authentication 객체에서 현재 접속 회원 정보를 얻어오는 유틸 클래스
 *
 * <p>시큐리티를 통해 가입한 회원만 사용하도록 강제한 api에서 로그인한 회원 정보가 필요한 경우 -> getLoginMemberXXX() 사용 가입하지 않은 유저와 회원 둘
 * 다 사용하는 api에서 회원 정보가 필요한 경우 -> getUserXXX() 사용
 */
public class SecurityUtil {
  private static final String ANONYMOUS_USER = "anonymousUser";

  public static Optional<Long> getUserId() {
    return getUserAuthDetails().map(CustomUserDetails::getMemberId);
  }

  public static Long getLoginMemberId() {
    return getLoginMemberAuthInfo().getMemberId();
  }

  //  public static Long getLoginMemberBreadTypeId() {
  //    return getLoginMemberAuthInfo().getBreadTypeId();
  //  }

  public static MainPurpose getLoginMemberMainPurpose() {
    return getLoginMemberAuthInfo().getMainPurpose();
  }

  //  public static Long getLoginMemberNutrientTypeId() {
  //    return getLoginMemberAuthInfo().getNutrientTypeId();
  //  }

  public static String getLoginMemberNickname() {
    return getLoginMemberAuthInfo().getNickname();
  }

  private static CustomUserDetails getLoginMemberAuthInfo() {
    return (CustomUserDetails)
        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  public static Map<String, Object> getLoginMemberInfo() {
    Map<String, Object> memberInfoMap = new HashMap<>();
    memberInfoMap.put("memberId", getLoginMemberId());
    memberInfoMap.put("mainPurpose", getLoginMemberMainPurpose());
    memberInfoMap.put("nickname", getLoginMemberNickname());

    return memberInfoMap;
  }

  private static Optional<CustomUserDetails> getUserAuthDetails() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal.equals(ANONYMOUS_USER)) {
      return Optional.empty();
    }
    return Optional.of((CustomUserDetails) principal);
  }

  //    public static Long getLoginMemberBreadTypeId() {
  //    }
}
