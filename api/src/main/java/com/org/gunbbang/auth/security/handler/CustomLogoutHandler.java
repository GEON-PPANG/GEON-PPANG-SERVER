package com.org.gunbbang.auth.security.handler;

import com.org.gunbbang.support.exception.BadRequestException;
import com.org.gunbbang.support.exception.NotFoundException;
import com.org.gunbbang.auth.security.util.SecurityUtil;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.support.errorType.ErrorType;
import com.org.gunbbang.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Slf4j
public class CustomLogoutHandler implements LogoutHandler {
  private final MemberRepository memberRepository;

  @Value("${jwt.access.header}")
  private String accessHeader;

  public CustomLogoutHandler(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  @Override
  public void logout(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    log.info("########## CustomLogoutHandler 진입 ##########");

    if (request.getHeader(accessHeader) == null) {
      throw new BadRequestException(ErrorType.NO_REQUEST_HEADER_EXCEPTION);
    }

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
