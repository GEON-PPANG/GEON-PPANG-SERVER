package com.org.gunbbang.service;

import com.org.gunbbang.*;
import com.org.gunbbang.auth.jwt.service.AppleJwtService;
import com.org.gunbbang.controller.DTO.response.*;
import com.org.gunbbang.entity.*;
import com.org.gunbbang.support.errorType.ErrorType;
import com.org.gunbbang.repository.*;
import com.org.gunbbang.support.exception.BadRequestException;
import com.org.gunbbang.support.exception.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final AppleJwtService appleJWTService;
  private final BookMarkRepository bookMarkRepository;

  @Transactional(readOnly = true)
  public ValidationResponseDTO checkDuplicatedNickname(String nickname) {
    if (memberRepository.findByNickname(nickname).isPresent()) {
      throw new BadRequestException(ErrorType.ALREADY_EXIST_NICKNAME_EXCEPTION);
    }

    return ValidationResponseDTO.builder().isAvailable(true).build();
  }

  @Transactional(readOnly = true)
  public ValidationResponseDTO checkDuplicatedEmail(String email) {
    if (!memberRepository.findAllByEmail(email).isEmpty()) {
      throw new BadRequestException(ErrorType.ALREADY_EXIST_EMAIL_EXCEPTION);
    }

    return ValidationResponseDTO.builder().isAvailable(true).build();
  }

  public MemberWithdrawResponseDTO withdraw(String appleRefreshToken, Long memberId)
      throws Exception {
    if (getMemberPlatformType(memberId) == PlatformType.APPLE) {
      appleJWTService.revokeAppleTokens(appleRefreshToken, memberId);
    }

    Long deletedMemberCount = memberRepository.deleteMemberByMemberId(memberId).get();
    if (deletedMemberCount == 0) {
      throw new NotFoundException(
          ErrorType.NOT_FOUND_USER_EXCEPTION,
          ErrorType.NOT_FOUND_USER_EXCEPTION.getMessage() + memberId);
    }

    List<BookMark> bookMarks = bookMarkRepository.findAllByMemberId(memberId);
    for (BookMark bookMark : bookMarks) {
      Bakery bookMarkedBakery = bookMark.getBakery();
      bookMarkedBakery.updateBookMarkCount(false); // 삭제될 북마크 객체 bakery에 카운트 반영
    }

    SecurityContextHolder.clearContext();
    log.info(
        "회원 탈퇴 성공. 탈퇴한 회원 id: {} || securityContext: {}",
        memberId,
        SecurityContextHolder.getContext());
    return MemberWithdrawResponseDTO.builder().memberId(memberId).build();
  }

  private PlatformType getMemberPlatformType(Long memberId) {
    Member foundMember =
        memberRepository
            .findById(memberId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorType.NOT_FOUND_USER_EXCEPTION,
                        ErrorType.NOT_FOUND_USER_EXCEPTION.getMessage() + memberId));
    return foundMember.getPlatformType();
  }

  public NicknameUpdateResponseDTO updateMemberNickname(Long memberId, String nickname) {
    if (memberRepository.findByNickname(nickname).isPresent()) {
      throw new BadRequestException(ErrorType.ALREADY_EXIST_NICKNAME_EXCEPTION);
    }
    Member foundMember =
        memberRepository
            .findById(memberId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorType.NOT_FOUND_USER_EXCEPTION,
                        ErrorType.NOT_FOUND_USER_EXCEPTION.getMessage() + memberId));

    foundMember.authorizeUser();
    foundMember.updateNickname(nickname);
    Member savedMember = memberRepository.saveAndFlush(foundMember);

    return NicknameUpdateResponseDTO.builder()
        .nickname(savedMember.getNickname())
        .role(savedMember.getRole())
        .memberId(savedMember.getMemberId())
        .build();
  }
}
