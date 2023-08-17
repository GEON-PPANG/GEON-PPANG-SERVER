package com.org.gunbbang.service;

import com.org.gunbbang.BadRequestException;
import com.org.gunbbang.BestReviewDTO;
import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.controller.DTO.request.RecommendKeywordNameRequestDTO;
import com.org.gunbbang.controller.DTO.request.ReviewRequestDTO;
import com.org.gunbbang.controller.DTO.response.*;
import com.org.gunbbang.entity.*;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.*;
import com.org.gunbbang.util.RecommendKeywordPercentage;
import com.org.gunbbang.util.mapper.BakeryMapper;
import com.org.gunbbang.util.mapper.BreadTypeMapper;
import com.org.gunbbang.util.mapper.RecommendKeywordMapper;
import com.org.gunbbang.util.mapper.ReviewMapper;
import com.org.gunbbang.util.security.SecurityUtil;
import java.time.format.DateTimeFormatter;
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
  private final BookMarkRepository bookMarkRepository;
  private final int maxBestBakeryCount = 10;

  public Long createReview(Long bakeryId, ReviewRequestDTO reviewRequestDto) {
    Long currentMemberId = SecurityUtil.getLoginMemberId();
    Member member =
        memberRepository
            .findById(currentMemberId)
            .orElseThrow(() -> new BadRequestException(ErrorType.TOKEN_TIME_EXPIRED_EXCEPTION));
    Bakery bakery =
        bakeryRepository
            .findById(bakeryId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_BAKERY_EXCEPTION));
    Review review =
        reviewRepository.saveAndFlush(
            Review.builder()
                .member(member)
                .bakery(bakery)
                .isLike(reviewRequestDto.getIsLike())
                .reviewText(reviewRequestDto.getReviewText())
                .build());

    if (reviewRequestDto.getIsLike()) {
      createReviewRecommendKeyword(reviewRequestDto.getKeywordList(), review.getReviewId());
    }

    bakery.reviewCountChange(true);
    bakeryRepository.saveAndFlush(bakery);
    return review.getReviewId();
  }

  private void createReviewRecommendKeyword(
      List<RecommendKeywordNameRequestDTO> keywordNameRequestDtoList, Long reviewId) {
    Review review =
        reviewRepository
            .findById(reviewId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_REVIEW_EXCEPTION));
    Bakery bakery =
        bakeryRepository
            .findById(review.getBakery().getBakeryId())
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_BAKERY_EXCEPTION));
    for (RecommendKeywordNameRequestDTO keyword : keywordNameRequestDtoList) {
      RecommendKeyword recommendKeyword =
          recommendKeywordRepository
              .findByKeywordName(keyword.getKeywordName().getMessage())
              .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_CATEGORY_EXCEPTION));
      reviewRecommendKeywordRepository.saveAndFlush(
          ReviewRecommendKeyword.builder()
              .recommendKeyword(recommendKeyword)
              .review(review)
              .build());
      bakery.keywordCountChange(keyword.getKeywordName().getMessage());
    }
    bakeryRepository.saveAndFlush(bakery);
  }

  public ReviewDetailResponseDTO getReviewedByMember(Long reviewId) {
    Long currentMemberId = SecurityUtil.getLoginMemberId();
    Review review =
        reviewRepository
            .findById(reviewId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_REVIEW_EXCEPTION));
    if (currentMemberId.equals(review.getMember().getMemberId())) {
      List<RecommendKeywordResponseDTO> recommendKeywordList =
          getReCommendKeywordListResponseDTO(review);
      return ReviewMapper.INSTANCE.toReviewDetailResponseDTO(review, recommendKeywordList);
    } else {
      throw new BadRequestException(ErrorType.REQUEST_VALIDATION_EXCEPTION);
    }
  }

  public ReviewListResponseDTO getBakeryReviewList(Long bakeryId) {
    Bakery bakery =
        bakeryRepository
            .findById(bakeryId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_BAKERY_EXCEPTION));
    List<Review> reviewList = reviewRepository.findAllByBakeryOrderByCreatedAtDesc(bakery);
    List<ReviewResponseDTO> reviewListDto = new ArrayList<>();
    long reviewCount = bakery.getReviewCount();
    String createdAt;

    for (Review review : reviewList) {
      List<RecommendKeywordResponseDTO> recommendKeywordList =
          getReCommendKeywordListResponseDTO(review);
      createdAt = review.getCreatedAt().format(DateTimeFormatter.ofPattern("yy.MM.dd"));
      reviewListDto.add(
          ReviewMapper.INSTANCE.toReviewResponseDTO(review, createdAt, recommendKeywordList));
    }

    RecommendKeywordPercentage recommendKeywordPercentage =
        RecommendKeywordPercentage.builder()
            .deliciousPercent(calculatorPercentage(reviewCount, bakery.getKeywordDeliciousCount()))
            .specialPercent(calculatorPercentage(reviewCount, bakery.getKeywordDeliciousCount()))
            .kindPercent(calculatorPercentage(reviewCount, bakery.getKeywordKindCount()))
            .zeroWastePercent(calculatorPercentage(reviewCount, bakery.getKeywordZeroWasteCount()))
            .build();

    return ReviewMapper.INSTANCE.toReviewListResponseDTO(
        recommendKeywordPercentage, reviewCount, reviewListDto);
  }

  private List<RecommendKeywordResponseDTO> getReCommendKeywordListResponseDTO(Review review) {
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
    BreadTypeResponseDTO breadType;
    BakeryListReviewedByMemberDTO bakeryListReviewedByMemberDto;

    for (Review review : reviewList) {
      breadType =
          BreadTypeMapper.INSTANCE.toBreadTypeResponseDTO(review.getBakery().getBreadType());
      bakeryListReviewedByMemberDto =
          BakeryMapper.INSTANCE.toListReviewedByMemberDTO(review.getBakery(), review, breadType);
      responseDtoList.add(bakeryListReviewedByMemberDto);
    }
    return responseDtoList;
  }

  public List<BestReviewListResponseDTO> getBestReviews(Long memberId) {
    List<Long> alreadyFoundReviewIds = new ArrayList<>();
    alreadyFoundReviewIds.add(-1L);

    Member foundMember =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION));

    List<BestReviewDTO> bestReviews = getBestReviews(foundMember);

    if (bestReviews.size() == maxBestBakeryCount) {
      log.info("베스트 리뷰 10개 조회 완료. 추가 조회 쿼리 없이 바로 반환");
      return getBestReviewsListResponseDTO(bestReviews);
    }

    log.info("랜덤 리뷰 조회 시작. 현재까지 조회된 리뷰 수: " + bestReviews.size());
    setAlreadyFoundReviewIds(alreadyFoundReviewIds, bestReviews);
    getRandomReviews(alreadyFoundReviewIds, bestReviews);

    return getBestReviewsListResponseDTO(bestReviews);
  }

  private void getRandomReviews(List<Long> alreadyFoundReviewIds, List<BestReviewDTO> bestReviews) {
    PageRequest restPageRequest = PageRequest.of(0, maxBestBakeryCount - bestReviews.size());
    bestReviews.addAll(
        reviewRepository.findRestBestReviewDTOListByBreadType(
            alreadyFoundReviewIds, restPageRequest));
  }

  private void setAlreadyFoundReviewIds(
      List<Long> alreadyFoundReviews, List<BestReviewDTO> bestReviews) {
    alreadyFoundReviews.addAll(
        bestReviews.stream().map(BestReviewDTO::getReviewId).collect(Collectors.toList()));
  }

  private List<BestReviewDTO> getBestReviews(Member foundMember) {
    PageRequest bestPageRequest = PageRequest.of(0, maxBestBakeryCount);
    List<BestReviewDTO> bestReviews =
        reviewRepository.findBestReviewDTOList(
            foundMember.getBreadType().getBreadTypeId(),
            foundMember.getMainPurpose(),
            bestPageRequest);
    return bestReviews;
  }

  private List<BestReviewListResponseDTO> getBestReviewsListResponseDTO(
      List<BestReviewDTO> bestReviews) {
    List<BestReviewListResponseDTO> responseDtoList = new ArrayList();
    for (BestReviewDTO bestReview : bestReviews) {
      String[] recommendKeywords = getMaxRecommendKeywords(bestReview);

      BestReviewListResponseDTO response =
          ReviewMapper.INSTANCE.toBestReviewListResponseDTO(
              bestReview, recommendKeywords[0], recommendKeywords[1]);

      responseDtoList.add(response);
    }
    return responseDtoList;
  }

  private String[] getMaxRecommendKeywords(BestReviewDTO bestReview) {

    Map<String, Long> recommendKeywordsMap = new HashMap<>();
    recommendKeywordsMap.put(
        com.org.gunbbang.RecommendKeyword.DELICIOUS.getMessage(),
        bestReview.getKeywordDeliciousCount());
    recommendKeywordsMap.put(
        com.org.gunbbang.RecommendKeyword.KIND.getMessage(), bestReview.getKeywordKindCount());
    recommendKeywordsMap.put(
        com.org.gunbbang.RecommendKeyword.SPECIAL_MENU.getMessage(),
        bestReview.getKeywordSpecialCount());
    recommendKeywordsMap.put(
        com.org.gunbbang.RecommendKeyword.ZERO_WASTE.getMessage(),
        bestReview.getKeywordZeroWasteCount());

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
