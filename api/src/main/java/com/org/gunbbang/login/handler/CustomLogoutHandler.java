package com.org.gunbbang.login.handler;

import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.MemberRepository;
import com.org.gunbbang.util.security.SecurityUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Slf4j
public class CustomLogoutHandler implements LogoutHandler {
  private final MemberRepository memberRepository;

  public CustomLogoutHandler(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  @Override
  public void logout(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    log.info("########## CustomLogoutHandler 진입 ##########");

    Long memberId = SecurityUtil.getLoginMemberId();
    Member foundMember =
        memberRepository
            .findById(memberId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorType.NOT_FOUND_USER_EXCEPTION,
                        ErrorType.NOT_FOUND_USER_EXCEPTION.getMessage() + memberId));
    foundMember.updateRefreshToken(null);
    memberRepository.saveAndFlush(foundMember);

    SecurityContextHolder.clearContext();

    log.info("########## 로그아웃 성공. memberId : {} ##########", memberId);
  }
}
