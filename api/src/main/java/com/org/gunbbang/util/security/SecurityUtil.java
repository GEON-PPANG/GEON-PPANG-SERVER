package com.org.gunbbang.util.security;

import com.org.gunbbang.MainPurpose;
import com.org.gunbbang.login.CustomUserDetails;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.context.SecurityContextHolder;

/** SecurityContextHolder로부터 얻은 Authentication 객체에서 현재 접속 회원 정보를 얻어오는 유틸 클래스 */
public class SecurityUtil {
  public static Long getLoginMemberId() {
    return getCustomUserDetails().getMemberId();
  }

  public static Long getLoginMemberBreadTypeId() {
    return getCustomUserDetails().getBreadTypeId();
  }

  public static MainPurpose getLoginMemberMainPurpose() {
    return getCustomUserDetails().getMainPurpose();
  }

  public static Long getLoginMemberNutrientTypeId() {
    return getCustomUserDetails().getNutrientTypeId();
  }

  public static String getLoginMemberNickname() {
    return getCustomUserDetails().getNickname();
  }

  public static Map<String, Object> getLoginMemberInfo() {
    Map<String, Object> memberInfoMap = new HashMap<>();
    memberInfoMap.put("memberId", getLoginMemberId());
    memberInfoMap.put("breadTypeId", getLoginMemberBreadTypeId());
    memberInfoMap.put("mainPurpose", getLoginMemberMainPurpose());
    memberInfoMap.put("nutrientTypeId", getLoginMemberNutrientTypeId());

    return memberInfoMap;
  }

  private static CustomUserDetails getCustomUserDetails() {
    CustomUserDetails userInfo =
        (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userInfo;
  }
}
