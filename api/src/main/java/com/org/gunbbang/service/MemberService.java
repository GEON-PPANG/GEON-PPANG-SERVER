package com.org.gunbbang.service;

import com.org.gunbbang.*;
import com.org.gunbbang.auth.jwt.service.AppleJwtService;
import com.org.gunbbang.auth.security.util.SecurityUtil;
import com.org.gunbbang.controller.DTO.request.MemberTypesRequestDTO;
import com.org.gunbbang.controller.DTO.response.*;
import com.org.gunbbang.entity.*;
import com.org.gunbbang.entity.BreadType;
import com.org.gunbbang.support.errorType.ErrorType;
import com.org.gunbbang.repository.*;
import com.org.gunbbang.support.exception.BadRequestException;
import com.org.gunbbang.support.exception.NotFoundException;
import com.org.gunbbang.util.mapper.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
  private final BreadTypeRepository breadTypeRepository;
  private final NutrientTypeRepository nutrientTypeRepository;
  private final AppleJwtService appleJWTService;
  private final BookMarkRepository bookMarkRepository;
  private final MemberBreadTypeRepository memberBreadTypeRepository;
  private final MemberNutrientTypeRepository memberNutrientTypeRepository;

  public MemberDetailResponseDTO getMemberDetail() {
    String memberNickname = SecurityUtil.getLoginMemberNickname();
    MainPurpose memberMainPurpose = SecurityUtil.getLoginMemberMainPurpose();
    // Long memberBreadTypeId = SecurityUtil.getLoginMemberBreadTypeId();
    Member foundMember =
        memberRepository
            .findByNickname(memberNickname)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorType.NOT_FOUND_USER_EXCEPTION,
                        ErrorType.NOT_FOUND_USER_EXCEPTION.getMessage() + memberNickname));

    List<MemberBreadType> breadType = memberBreadTypeRepository.findAllByMember(foundMember);
    List<BreadTypeResponseDTO> breadTypeResponseDTO =
        MemberBreadTypeMapper.INSTANCE.toBreadTypeResponseDTOList(breadType);

    return MemberTypeMapper.INSTANCE.toMemberDetailResponseDTO(
        memberNickname, memberMainPurpose, breadTypeResponseDTO);
  }

  public MemberTypeResponseDTO updateMemberTypes(
      MemberTypesRequestDTO request, Long memberId, String nickname) {
    Member foundMember =
        memberRepository
            .findById(memberId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorType.NOT_FOUND_USER_EXCEPTION,
                        ErrorType.NOT_FOUND_USER_EXCEPTION.getMessage() + memberId));

    if (memberBreadTypeRepository.existsByMember(foundMember)) {
      memberBreadTypeRepository.deleteAllByMember(foundMember); // 기존에 있던 MemberBreadType 전부 삭제
    }

    List<MemberBreadType> memberBreadTypes =
        saveBreadTypes(request.getBreadTypeList(), foundMember);

    if (memberNutrientTypeRepository.existsByMember(foundMember)) {
      memberNutrientTypeRepository.deleteAllByMember(
          foundMember); // 기존에 있던 MemberNutrientType 전부 삭제
    }

    List<MemberNutrientType> memberNutrientTypes =
        saveNutrientTypes(request.getNutrientTypeList(), foundMember);

    foundMember.updateMainPurpose(request.getMainPurpose());
    memberRepository.saveAndFlush(foundMember);

    List<BreadTypeResponseDTO> breadTypeResponseDTO =
        MemberBreadTypeMapper.INSTANCE.toBreadTypeResponseDTOList(memberBreadTypes);
    List<NutrientTypeResponseDTO> nutrientTypeResponseDTO =
        MemberNutrientTypeMapper.INSTANCE.toNutrientTypeResponseDTOList(memberNutrientTypes);

    return MemberTypeMapper.INSTANCE.toMemberTypeResponseDTO(
        foundMember.getMemberId(),
        foundMember.getMainPurpose(),
        nickname,
        breadTypeResponseDTO,
        nutrientTypeResponseDTO);
  }

  private List<MemberBreadType> saveBreadTypes(List<Long> breadTypeIds, Member foundMember) {
    List<MemberBreadType> memberBreadTypes = new ArrayList<>();

    for (Long breadTypeId : breadTypeIds) {
      memberBreadTypes.add(createMemberBreadTypeById(foundMember, breadTypeId));
    }
    return memberBreadTypeRepository.saveAllAndFlush(memberBreadTypes);
  }

  private MemberBreadType createMemberBreadTypeById(Member foundMember, Long breadTypeId) {
    BreadType foundBreadType =
        breadTypeRepository
            .findByBreadTypeId(breadTypeId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_BREAD_TYPE_EXCEPTION));

    MemberBreadType memberBreadType =
        MemberBreadType.builder().member(foundMember).breadType(foundBreadType).build();
    return memberBreadType;
  }

  private List<MemberNutrientType> saveNutrientTypes(
      List<Long> nutrientTypeIds, Member foundMember) {
    List<MemberNutrientType> memberNutrientTypes = new ArrayList<>();

    for (Long nutrientTypeId : nutrientTypeIds) {
      memberNutrientTypes.add(createMemberNutrientTypeById(foundMember, nutrientTypeId));
    }

    return memberNutrientTypeRepository.saveAllAndFlush(memberNutrientTypes);
  }

  private MemberNutrientType createMemberNutrientTypeById(Member foundMember, Long nutrientTypeId) {
    NutrientType foundNutrientType =
        nutrientTypeRepository
            .findByNutrientTypeId(nutrientTypeId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_NUTRIENT_EXCEPTION));

    MemberNutrientType memberNutrientType =
        MemberNutrientType.builder().member(foundMember).nutrientType(foundNutrientType).build();
    return memberNutrientType;
  }

  public MemberTypeResponseDTO getMemberTypes(Map<String, Object> loginMemberInfo) {
    Long memberId = Long.parseLong(loginMemberInfo.get("memberId").toString());
    Member member =
        memberRepository
            .findById(memberId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorType.NOT_FOUND_USER_EXCEPTION,
                        ErrorType.NOT_FOUND_USER_EXCEPTION.getMessage() + memberId));
    List<MemberBreadType> memberBreadTypes = memberBreadTypeRepository.findAllByMember(member);
    List<MemberNutrientType> memberNutrientTypes =
        memberNutrientTypeRepository.findAllByMemberId(memberId);

    List<BreadTypeResponseDTO> breadTypeResponseDTO =
        MemberBreadTypeMapper.INSTANCE.toBreadTypeResponseDTOList(memberBreadTypes);
    List<NutrientTypeResponseDTO> nutrientTypeResponseDTO =
        MemberNutrientTypeMapper.INSTANCE.toNutrientTypeResponseDTOList(memberNutrientTypes);

    return MemberTypeMapper.INSTANCE.toMemberTypeResponseDTO(
        memberId,
        (MainPurpose) loginMemberInfo.get("mainPurpose"),
        loginMemberInfo.get("nickname").toString(),
        breadTypeResponseDTO,
        nutrientTypeResponseDTO);
  }

  public ValidationResponseDTO checkDuplicatedNickname(String nickname) {
    if (memberRepository.findByNickname(nickname).isPresent()) {
      throw new BadRequestException(ErrorType.ALREADY_EXIST_NICKNAME_EXCEPTION);
    }

    return ValidationResponseDTO.builder().isAvailable(true).build();
  }

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
