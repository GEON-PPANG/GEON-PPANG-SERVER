package com.org.gunbbang.service;

import com.org.gunbbang.BadRequestException;
import com.org.gunbbang.DTO.BestReviewDTO;
import com.org.gunbbang.MainPurpose;
import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.auth.security.util.SecurityUtil;
import com.org.gunbbang.controller.DTO.request.RecommendKeywordNameRequestDTO;
import com.org.gunbbang.controller.DTO.request.ReviewRequestDTO;
import com.org.gunbbang.controller.DTO.response.*;
import com.org.gunbbang.entity.*;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.*;
import com.org.gunbbang.util.RecommendKeywordPercentage;
import com.org.gunbbang.util.mapper.*;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
  private final ReviewRepository reviewRepository;
  private final ReviewRecommendKeywordRepository reviewRecommendKeywordRepository;
  private final BakeryRepository bakeryRepository;
  private final MemberRepository memberRepository;
  private final RecommendKeywordRepository recommendKeywordRepository;
  private final MemberBreadTypeRepository memberBreadTypeRepository;
  private final MemberNutrientTypeRepository memberNutrientTypeRepository;
  private final BakeryBreadTypeRepository bakeryBreadTypeRepository;
  private final int maxBestBakeryCount = 10;

  public Long createReview(Long currentMemberId, Long bakeryId, ReviewRequestDTO reviewRequestDto) {
    String reviewText = reviewRequestDto.getReviewText().trim();

    validateReview(reviewRequestDto, reviewText);

    Member member =
        memberRepository
            .findById(currentMemberId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorType.NOT_FOUND_USER_EXCEPTION,
                        ErrorType.NOT_FOUND_USER_EXCEPTION.getMessage() + currentMemberId));
    Bakery bakery =
        bakeryRepository
            .findById(bakeryId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorType.NOT_FOUND_BAKERY_EXCEPTION,
                        ErrorType.NOT_FOUND_BAKERY_EXCEPTION.getMessage() + bakeryId));
    Review review =
        reviewRepository.saveAndFlush(
            Review.builder()
                .member(member)
                .bakery(bakery)
                .isLike(reviewRequestDto.getIsLike())
                .reviewText(reviewText)
                .build());

    if (reviewRequestDto.getIsLike()) {
      createReviewRecommendKeyword(reviewRequestDto.getKeywordList(), review, bakery);
    }

    bakery.reviewCountChange(true);
    bakeryRepository.saveAndFlush(bakery);
    return review.getReviewId();
  }

  private void validateReview(ReviewRequestDTO reviewRequestDto, String reviewText) {
    if (reviewRequestDto.getIsLike() && reviewRequestDto.getKeywordList().isEmpty()) {
      throw new BadRequestException(ErrorType.REQUEST_KEYWORDLIST_VALIDATION_EXCEPTION);
    }
    if (!reviewRequestDto.getIsLike()) {
      if (!reviewRequestDto.getKeywordList().isEmpty()) {
        throw new BadRequestException(ErrorType.REQUEST_ISNOTLIKE_KEYWORDLIST_VALIDATION_EXCEPTION);
      }
      if (reviewText.equals("")) {
        throw new BadRequestException(ErrorType.REQUEST_ISNOTLIKE_REVIEWTEXT_VALIDATION_EXCEPTION);
      }
    }
  }

  private void createReviewRecommendKeyword(
      List<RecommendKeywordNameRequestDTO> keywordNameRequestDtoList,
      Review review,
      Bakery bakery) {

    for (RecommendKeywordNameRequestDTO keyword : keywordNameRequestDtoList) {
      RecommendKeyword recommendKeyword =
          recommendKeywordRepository
              .findByKeywordName(keyword.getKeywordName().getMessage())
              .orElseThrow(
                  () ->
                      new NotFoundException(
                          ErrorType.NOT_FOUND_CATEGORY_EXCEPTION,
                          ErrorType.NOT_FOUND_CATEGORY_EXCEPTION.getMessage()
                              + keyword.getKeywordName().getMessage()));

      reviewRecommendKeywordRepository.saveAndFlush(
          ReviewRecommendKeyword.builder()
              .recommendKeyword(recommendKeyword)
              .review(review)
              .build());
      bakery.keywordCountChange(keyword.getKeywordName().getMessage());
    }

    bakeryRepository.saveAndFlush(bakery);
  }

  public ReviewDetailResponseDTO getReviewedByMember(Long reviewId, Long memberId) {
    Review review =
        reviewRepository
            .findByReviewId(reviewId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorType.NOT_FOUND_REVIEW_EXCEPTION,
                        ErrorType.NOT_FOUND_REVIEW_EXCEPTION.getMessage() + reviewId));
    if (memberId.equals(review.getMember().getMemberId())) {
      List<RecommendKeywordResponseDTO> recommendKeywordList =
          getRecommendKeywordListResponseDTO(review);
      return ReviewMapper.INSTANCE.toReviewDetailResponseDTO(review, recommendKeywordList);
    } else {
      throw new BadRequestException(ErrorType.REQUEST_VALIDATION_EXCEPTION);
    }
  }

  public ReviewListResponseDTO getBakeryReviewList(Long bakeryId) {
    Bakery bakery =
        bakeryRepository
            .findById(bakeryId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorType.NOT_FOUND_BAKERY_EXCEPTION,
                        ErrorType.NOT_FOUND_BAKERY_EXCEPTION.getMessage() + bakeryId));
    List<Review> reviewList = reviewRepository.findAllByBakeryOrderByCreatedAtDesc(bakery);
    List<ReviewResponseDTO> reviewListDto = new ArrayList<>();
    long reviewCount = bakery.getReviewCount();

    for (Review review : reviewList) {
      List<RecommendKeywordResponseDTO> recommendKeywordList =
          getRecommendKeywordListResponseDTO(review);
      reviewListDto.add(ReviewMapper.INSTANCE.toReviewResponseDTO(review, recommendKeywordList));
    }

    RecommendKeywordPercentage recommendKeywordPercentage =
        RecommendKeywordPercentage.builder()
            .deliciousPercent(calculatorPercentage(reviewCount, bakery.getKeywordDeliciousCount()))
            .specialPercent(calculatorPercentage(reviewCount, bakery.getKeywordSpecialCount()))
            .kindPercent(calculatorPercentage(reviewCount, bakery.getKeywordKindCount()))
            .zeroWastePercent(calculatorPercentage(reviewCount, bakery.getKeywordZeroWasteCount()))
            .build();

    return ReviewMapper.INSTANCE.toReviewListResponseDTO(
        recommendKeywordPercentage, reviewCount, reviewListDto);
  }

  private List<RecommendKeywordResponseDTO> getRecommendKeywordListResponseDTO(Review review) {
    List<RecommendKeywordResponseDTO> recommendKeywordList = new ArrayList<>();
    if (review.getIsLike()) {
      List<ReviewRecommendKeyword> reviewRecommendKeywordList =
          reviewRecommendKeywordRepository.findAllByReview(review);
      return RecommendKeywordMapper.INSTANCE.toRecommendKeywordListResponseDTO(
          reviewRecommendKeywordList);
    }
    return recommendKeywordList;
  }

  private float calculatorPercentage(long reviewCount, long keywordCount) {
    if (reviewCount == 0) {
      return 0f;
    }
    return keywordCount / (float) reviewCount;
  }

  public List<BakeryListReviewedByMemberDTO> getBakeryListReviewedByMember(Long memberId) {
    Member currentMember =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new BadRequestException(ErrorType.REQUEST_VALIDATION_EXCEPTION));
    List<Review> reviewList = reviewRepository.findAllByMemberOrderByCreatedAtDesc(currentMember);
    List<BakeryListReviewedByMemberDTO> responseDtoList = new ArrayList<>();
    List<BreadTypeResponseDTO> breadType;
    BakeryListReviewedByMemberDTO bakeryListReviewedByMemberDto;

    for (Review review : reviewList) {
      breadType =
          BakeryBreadTypeMapper.INSTANCE.toBreadTypeResponseDTOList(
              bakeryBreadTypeRepository.findAllByBakery(review.getBakery()));
      bakeryListReviewedByMemberDto =
          BakeryMapper.INSTANCE.toListReviewedByMemberDTO(review.getBakery(), review, breadType);
      responseDtoList.add(bakeryListReviewedByMemberDto);
    }
    return responseDtoList;
  }

  public List<BestReviewListResponseDTO> getBestReviews() {
    Optional<Long> memberId = SecurityUtil.getUserId();

    if (memberId.isEmpty()) {
      List<Review> randomReviews = getRandomReviews();
      return getRandomReviewListResponseDTOs(randomReviews);
    }

    Member foundMember =
        memberRepository
            .findById(memberId.get())
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorType.NOT_FOUND_USER_EXCEPTION,
                        ErrorType.NOT_FOUND_USER_EXCEPTION.getMessage() + memberId));

    if (!isFilterSelected(foundMember)) {
      log.info("########## 회원이 필터 선택 안한 경우. 랜덤으로 10개 리뷰 반환 ##########");
      List<Review> randomReviews = getRandomReviews();
      return getRandomReviewListResponseDTOs(randomReviews);
    }

    List<BreadType> breadTypes =
        memberBreadTypeRepository.findAllByMember(foundMember).stream()
            .map(MemberBreadType::getBreadType)
            .collect(Collectors.toList());

    List<BestReviewDTO> bestReviews = getBestReviews(foundMember, breadTypes);
    if (bestReviews.size() == maxBestBakeryCount) {
      log.info("########## 베스트 리뷰 10개 조회 완료. 추가 조회 쿼리 없이 바로 반환 ##########");
      return getBestReviewListResponseDTOs(bestReviews);
    }

    log.info("########## 랜덤 리뷰 조회 시작. 현재까지 조회된 리뷰 수: {} ##########", bestReviews.size());
    List<Long> alreadyFoundReviewIds = new ArrayList<>();
    alreadyFoundReviewIds.add(-1L);
    getRestReviewsRandomly(alreadyFoundReviewIds, bestReviews);

    return getBestReviewListResponseDTOs(bestReviews);
  }

  private List<Review> getRandomReviews() {
    PageRequest randomPageRequest = PageRequest.of(0, maxBestBakeryCount);
    List<Review> randomReviews =
        reviewRepository.findRandomReviews(randomPageRequest); // 랜덤 리뷰 10개 조회
    return randomReviews;
  }

  private boolean isFilterSelected(Member foundMember) {
    boolean isBreadTypeSelected = memberBreadTypeRepository.existsByMember(foundMember);
    boolean isNutrientTypeSelected = memberNutrientTypeRepository.existsByMember(foundMember);
    boolean isMainPurposeSelected = !foundMember.getMainPurpose().equals(MainPurpose.NONE);

    return isBreadTypeSelected && isNutrientTypeSelected && isMainPurposeSelected;
  }

  private void getRestReviewsRandomly(
      List<Long> alreadyFoundReviewIds, List<BestReviewDTO> bestReviews) {
    PageRequest restPageRequest = PageRequest.of(0, maxBestBakeryCount - bestReviews.size());
    setAlreadyFoundReviewIds(alreadyFoundReviewIds, bestReviews);
    bestReviews.addAll(
        reviewRepository.findRestBestReviewDTOListByBreadType(
            alreadyFoundReviewIds, restPageRequest));
  }

  private void setAlreadyFoundReviewIds(
      List<Long> alreadyFoundReviews, List<BestReviewDTO> bestReviews) {
    alreadyFoundReviews.addAll(
        bestReviews.stream().map(BestReviewDTO::getReviewId).collect(Collectors.toList()));
  }

  private List<BestReviewDTO> getBestReviews(Member foundMember, List<BreadType> breadTypes) {
    PageRequest bestPageRequest = PageRequest.of(0, maxBestBakeryCount);
    List<BestReviewDTO> bestReviews =
        reviewRepository.findBestReviewDTOList(
            breadTypes, foundMember.getMainPurpose()); // , bestPageRequest);
    return bestReviews;
  }

  private List<BestReviewListResponseDTO> getRandomReviewListResponseDTOs(
      List<Review> randomReviews) {
    List<BestReviewListResponseDTO> responseDtoList = new ArrayList();
    for (Review randomReview : randomReviews) {
      String[] recommendKeywords =
          getMaxRecommendKeywords(
              randomReview.getBakery().getKeywordDeliciousCount(),
              randomReview.getBakery().getKeywordKindCount(),
              randomReview.getBakery().getKeywordSpecialCount(),
              randomReview.getBakery().getKeywordZeroWasteCount());

      BestReviewListResponseDTO response =
          ReviewMapper.INSTANCE.toBestReviewListResponseDTO(
              randomReview, randomReview.getBakery(), recommendKeywords[0], recommendKeywords[1]);

      responseDtoList.add(response);
    }
    return responseDtoList;
  }

  private List<BestReviewListResponseDTO> getBestReviewListResponseDTOs(
      List<BestReviewDTO> bestReviews) {
    List<BestReviewListResponseDTO> responseDtoList = new ArrayList();
    for (BestReviewDTO bestReview : bestReviews) {
      String[] recommendKeywords =
          getMaxRecommendKeywords(
              bestReview.getKeywordDeliciousCount(),
              bestReview.getKeywordKindCount(),
              bestReview.getKeywordSpecialCount(),
              bestReview.getKeywordZeroWasteCount());

      BestReviewListResponseDTO response =
          ReviewMapper.INSTANCE.toBestReviewListResponseDTO(
              bestReview, recommendKeywords[0], recommendKeywords[1]);

      responseDtoList.add(response);
    }
    return responseDtoList;
  }

  private String[] getMaxRecommendKeywords(
      Long deliciousCount, Long kindCount, Long specialCount, Long zeroWaste) {

    Map<String, Long> recommendKeywordsMap = new HashMap<>();
    recommendKeywordsMap.put(
        com.org.gunbbang.RecommendKeyword.DELICIOUS.getMessage(), deliciousCount);
    recommendKeywordsMap.put(com.org.gunbbang.RecommendKeyword.KIND.getMessage(), kindCount);
    recommendKeywordsMap.put(
        com.org.gunbbang.RecommendKeyword.SPECIAL_MENU.getMessage(), specialCount);
    recommendKeywordsMap.put(com.org.gunbbang.RecommendKeyword.ZERO_WASTE.getMessage(), zeroWaste);

    Long maxValue = Long.MIN_VALUE;
    Long secondMaxValue = Long.MAX_VALUE;
    String maxKey = "null";
    String secondMaxKey = "null";

    for (Map.Entry<String, Long> entry : recommendKeywordsMap.entrySet()) {
      Long value = entry.getValue();

      if (value > maxValue) {
        secondMaxValue = maxValue;
        secondMaxKey = maxKey;
        maxValue = value;
        maxKey = entry.getKey();
      } else if (value > secondMaxValue) {
        secondMaxValue = value;
        secondMaxKey = entry.getKey();
      }
    }

    if (secondMaxValue == 0) {
      secondMaxKey = null;
    }

    return new String[] {maxKey, secondMaxKey};
  }
}
