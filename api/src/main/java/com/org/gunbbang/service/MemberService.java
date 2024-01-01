package com.org.gunbbang.service;

import com.org.gunbbang.*;
import com.org.gunbbang.auth.jwt.service.AppleJwtService;
import com.org.gunbbang.auth.security.util.SecurityUtil;
import com.org.gunbbang.controller.DTO.request.BreadTypeRequestDTO;
import com.org.gunbbang.controller.DTO.request.MemberTypesRequestDTO;
import com.org.gunbbang.controller.DTO.request.NutrientTypeRequestDTO;
import com.org.gunbbang.controller.DTO.response.*;
import com.org.gunbbang.entity.*;
import com.org.gunbbang.entity.BreadType;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.*;
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
    List<MemberBreadType> newMemberBreadTypes =
        createBreadType(request.getBreadType(), foundMember);

    if (memberNutrientTypeRepository.existsByMember(foundMember)) {
      memberNutrientTypeRepository.deleteAllByMember(foundMember); // 기존에 있던 MemberBreadType 전부 삭제
    }
    List<MemberNutrientType> newMemberNutrientTypes =
        createNutrientType(request.getNutrientType(), foundMember);

    foundMember.updateMainPurpose(request.getMainPurpose());
    memberRepository.saveAndFlush(foundMember);

    List<BreadTypeResponseDTO> breadTypeResponseDTOList =
        MemberBreadTypeMapper.INSTANCE.toBreadTypeResponseDTOList(newMemberBreadTypes);
    List<NutrientTypeResponseDTO> nutrientTypeResponseDTO =
        MemberNutrientTypeMapper.INSTANCE.toNutrientTypeResponseDTOList(newMemberNutrientTypes);

    return MemberTypeMapper.INSTANCE.toMemberTypeResponseDTO(
        foundMember.getMemberId(),
        foundMember.getMainPurpose(),
        nickname,
        breadTypeResponseDTOList,
        nutrientTypeResponseDTO);
  }

  private List<MemberBreadType> createBreadType(
      BreadTypeRequestDTO breadTypeRequest, Member foundMember) {
    List<MemberBreadType> memberBreadTypes = new ArrayList<>();
    if (breadTypeRequest.getIsGlutenFree()) {
      memberBreadTypes.add(createMemberBreadTypeByTag(foundMember, BreadTypeTag.GLUTEN_FREE));
    }

    if (breadTypeRequest.getIsVegan()) {
      memberBreadTypes.add(createMemberBreadTypeByTag(foundMember, BreadTypeTag.VEGAN));
    }

    if (breadTypeRequest.getIsNutFree()) {
      memberBreadTypes.add(createMemberBreadTypeByTag(foundMember, BreadTypeTag.NUT_FREE));
    }

    if (breadTypeRequest.getIsSugarFree()) {
      memberBreadTypes.add(createMemberBreadTypeByTag(foundMember, BreadTypeTag.SUGAR_FREE));
    }
    return memberBreadTypeRepository.saveAllAndFlush(memberBreadTypes);
  }

  private MemberBreadType createMemberBreadTypeByTag(
      Member foundMember, BreadTypeTag breadTypeTag) {
    BreadType foundBreadType =
        breadTypeRepository
            .findByBreadTypeTag(breadTypeTag)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_BREAD_TYPE_EXCEPTION));

    MemberBreadType memberBreadType =
        MemberBreadType.builder().member(foundMember).breadType(foundBreadType).build();
    return memberBreadType;
  }

  private List<MemberNutrientType> createNutrientType(
      NutrientTypeRequestDTO NutrientTypeRequest, Member foundMember) {
    List<MemberNutrientType> memberNutrientTypes = new ArrayList<>();
    if (NutrientTypeRequest.getIsNutrientOpen()) {
      memberNutrientTypes.add(
          createMemberNutrientTypeByTag(foundMember, NutrientTypeTag.NUTRIENT_OPEN));
    }

    if (NutrientTypeRequest.getIsIngredientOpen()) {
      memberNutrientTypes.add(
          createMemberNutrientTypeByTag(foundMember, NutrientTypeTag.INGREDIENT_OPEN));
    }

    if (NutrientTypeRequest.getIsNotOpen()) {
      memberNutrientTypes.add(createMemberNutrientTypeByTag(foundMember, NutrientTypeTag.NOT_OPEN));
    }
    return memberNutrientTypeRepository.saveAllAndFlush(memberNutrientTypes);
  }

  private MemberNutrientType createMemberNutrientTypeByTag(
      Member foundMember, NutrientTypeTag nutrientTypeTag) {
    NutrientType foundNutrientType =
        nutrientTypeRepository
            .findByNutrientTypeTag(nutrientTypeTag)
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
