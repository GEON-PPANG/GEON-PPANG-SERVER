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
import com.org.gunbbang.util.Security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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


    public Long createReview(Long bakeryId, ReviewRequestDTO reviewRequestDto){
        Long currentMemberId = SecurityUtil.getLoginMemberId();
        Member member = memberRepository.findById(currentMemberId).orElseThrow(()->new BadRequestException(ErrorType.TOKEN_TIME_EXPIRED_EXCEPTION));
        Bakery bakery = bakeryRepository.findById(bakeryId).orElseThrow(()->new NotFoundException(ErrorType.NOT_FOUND_BAKERY_EXCEPTION));
        Review review = reviewRepository.saveAndFlush(Review.builder()
                        .memberId(member)
                        .bakeryId(bakery)
                        .isLike(reviewRequestDto.getIsLike())
                        .reviewText(reviewRequestDto.getReviewText())
                .build());
        // 리뷰 작성되면 review count 증가
        bakery.reviewCountChange(true);
        bakeryRepository.saveAndFlush(bakery);
        return review.getReviewId();
    }

    public void createReviewRecommendKeyword(List<RecommendKeywordNameRequestDTO> keywordNameRequestDtoList, Long reviewId){
        Review review = reviewRepository.findById(reviewId).orElseThrow(()->new NotFoundException(ErrorType.NOT_FOUND_REVIEW));
        Bakery bakery = bakeryRepository.findById(review.getBakery().getBakeryId()).orElseThrow(()->new NotFoundException(ErrorType.NOT_FOUND_BAKERY_EXCEPTION));
        for(RecommendKeywordNameRequestDTO keyword : keywordNameRequestDtoList){
            RecommendKeyword recommendKeyword = recommendKeywordRepository.findByKeywordName(keyword.getKeywordName().getMessage()).orElseThrow(()->new NotFoundException(ErrorType.NOT_FOUND_CATEGORY_EXCEPTION));
            reviewRecommendKeywordRepository.saveAndFlush(ReviewRecommendKeyword.builder()
                            .recommendKeyword(recommendKeyword)
                            .review(review)
                    .build());
            bakery.keywordCountChange(keyword.getKeywordName().getMessage());
        }
        bakeryRepository.saveAndFlush(bakery);
    }

    public ReviewDetailResponseDTO getReviewedByMember(Long reviewId){
        Long currentMemberId = SecurityUtil.getLoginMemberId();
        Review review = reviewRepository.findById(reviewId).orElseThrow(()->new NotFoundException(ErrorType.NOT_FOUND_REVIEW));
        if(currentMemberId.equals(review.getMember().getMemberId())){
            List<RecommendKeywordResponseDTO> recommendKeywordList = new ArrayList<>();
            if (review.getIsLike()) {
                List<ReviewRecommendKeyword> reviewRecommendKeywordList = reviewRecommendKeywordRepository.findAllByReview(review);
                for (ReviewRecommendKeyword reviewRecommendKeyword : reviewRecommendKeywordList) {
                    recommendKeywordList.add(RecommendKeywordResponseDTO.builder()
                            .recommendKeywordId(reviewRecommendKeyword.getRecommendKeyword().getRecommendKeywordId())
                            .recommendKeywordName(reviewRecommendKeyword.getRecommendKeyword().getKeywordName())
                            .build());
                }
            }
            return ReviewDetailResponseDTO.builder()
                    .reviewId(review.getReviewId())
                    .isLike(review.getIsLike())
                    .recommendKeywordList(recommendKeywordList)
                    .reviewText(review.getReviewText())
                    .build();
        }
        else{
            throw new BadRequestException(ErrorType.REQUEST_VALIDATION_EXCEPTION);
        }
    }

    public ReviewListResponseDTO getBakeryReviewList(Long bakeryId){
        Bakery bakery = bakeryRepository.findById(bakeryId).orElseThrow(()->new NotFoundException(ErrorType.NOT_FOUND_BAKERY_EXCEPTION));
        List<Review> reviewList = reviewRepository.findAllByBakeryOrderByCreatedAtDesc(bakery);
        List<ReviewResponseDTO> reviewListDto = new ArrayList<>();
        Long reviewCount;

        for(Review review : reviewList){
            List<RecommendKeywordResponseDTO> recommendKeywordList = new ArrayList<>();
            if(review.getIsLike()) {
                List<ReviewRecommendKeyword> reviewRecommendKeywordList = reviewRecommendKeywordRepository.findAllByReview(review);
                for(ReviewRecommendKeyword reviewRecommendKeyword : reviewRecommendKeywordList){
                    recommendKeywordList.add(RecommendKeywordResponseDTO.builder()
                                    .recommendKeywordId(reviewRecommendKeyword.getRecommendKeyword().getRecommendKeywordId())
                                    .recommendKeywordName(reviewRecommendKeyword.getRecommendKeyword().getKeywordName())
                            .build());
                }
            }
            reviewListDto.add(ReviewResponseDTO.builder()
                    .reviewId(review.getReviewId())
                    .memberNickname(review.getMember().getNickname())
                    .recommendKeywordList(recommendKeywordList)
                    .reviewText(review.getReviewText())
                    .createdAt(review.getCreatedAt().format(DateTimeFormatter.ofPattern("yy.MM.dd")))
                    .build());
        }

        reviewCount = bakery.getReviewCount();

        return ReviewListResponseDTO.builder()
                .tastePercent(calculatorPercentage(reviewCount, bakery.getKeywordDeliciousCount()))
                .specialPercent(calculatorPercentage(reviewCount, bakery.getKeywordSpecialCount()))
                .kindPercent(calculatorPercentage(reviewCount, bakery.getKeywordKindCount()))
                .zeroPercent(calculatorPercentage(reviewCount, bakery.getKeywordZeroWasteCount()))
                .totalReviewCount(bakery.getReviewCount().intValue())
                .reviewList(reviewListDto)
                .build();
    }

    private Float calculatorPercentage(Long reviewCount, Long keywordCount){
        if(reviewCount==0){
            return 0f;
        }
        return keywordCount/(float)reviewCount;
    }

    public List<BakeryListReviewedByMemberDTO> getBakeryListReviewedByMember(Long memberId) {
        Member currentMember = memberRepository.findById(memberId).orElseThrow(() -> new BadRequestException(ErrorType.REQUEST_VALIDATION_EXCEPTION));
        List<Review> reviewList = reviewRepository.findAllByMemberOrderByCreatedAtDesc(currentMember);
        List<BakeryListReviewedByMemberDTO> responseDtoList = new ArrayList<>();
        BreadTypeResponseDTO breadType;
        BakeryListReviewedByMemberDTO bakeryListReviewedByMemberDto;

        for (Review review : reviewList) {
            breadType = BreadTypeResponseDTO.builder()
                    .breadTypeId(review.getBakery().getBreadType().getBreadTypeId())
                    .breadTypeName(review.getBakery().getBreadType().getBreadTypeName())
                    .isGlutenFree(review.getBakery().getBreadType().getIsGlutenFree())
                    .isVegan(review.getBakery().getBreadType().getIsVegan())
                    .isNutFree(review.getBakery().getBreadType().getIsNutFree())
                    .isSugarFree(review.getBakery().getBreadType().getIsSugarFree())
                    .build();

            bakeryListReviewedByMemberDto = BakeryListReviewedByMemberDTO.builder()
                    .bakeryId(review.getBakery().getBakeryId())
                    .bakeryName(review.getBakery().getBakeryName())
                    .bakeryPicture(review.getBakery().getBakeryPicture())
                    .isHACCP(review.getBakery().getIsHACCP())
                    .isVegan(review.getBakery().getIsVegan())
                    .isNonGMO(review.getBakery().getIsNonGMO())
                    .breadType(breadType)
                    .firstNearStation(review.getBakery().getFirstNearStation())
                    .secondNearStation(review.getBakery().getSecondNearStation())
                    .reviewId(review.getReviewId())
                    .createdAt(review.getCreatedAt().format(DateTimeFormatter.ofPattern("yy.MM.dd")))
                    .build();

            responseDtoList.add(bakeryListReviewedByMemberDto);
        }
        return responseDtoList;
    }

    public List<BestReviewListResponseDTO> getBestReviews(Long memberId) {
        List<Long> alreadyFoundReviews = new ArrayList<>();
        alreadyFoundReviews.add(Long.MAX_VALUE);

        PageRequest bestPageRequest = PageRequest.of(0, maxBestBakeryCount);
        Member foundMember = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION));

        List<BestReviewDTO> bestReviews = reviewRepository.findBestReviewDTOList(
                foundMember.getBreadType().getBreadTypeId(),
                foundMember.getMainPurpose(),
                bestPageRequest);

        if (bestReviews.size() == maxBestBakeryCount){
            return getBestReviewsListResponseDTO(memberId, bestReviews);
        }

        alreadyFoundReviews.addAll(
                bestReviews.stream().map(BestReviewDTO::getReviewId).collect(Collectors.toList()));
        PageRequest restPageRequest = PageRequest.of(0, maxBestBakeryCount - alreadyFoundReviews.size());

        bestReviews.addAll(
                reviewRepository.findRestBestReviewDTOListByBreadType(alreadyFoundReviews, restPageRequest)
        );

        return getBestReviewsListResponseDTO(memberId, bestReviews);
    }

    // TODO: 이거 DTO 안에 static 메서드로 못빼나??
    private List<BestReviewListResponseDTO> getBestReviewsListResponseDTO(Long memberId, List<BestReviewDTO> bestReviews) {
        List<BestReviewListResponseDTO> responseDtoList = new ArrayList();
        for (BestReviewDTO bestReview: bestReviews) {
            Boolean isBookMarked = isBookMarked(memberId, bestReview.getBakeryId());
            List<String> recommendKeywords = getMaxRecommendKeywords(bestReview);

            // max인 리뷰 키워드 두 개 넣기
            BestReviewListResponseDTO response = BestReviewListResponseDTO.builder()
                    .bakeryId(bestReview.getBakeryId())
                    .bakeryName(bestReview.getBakeryName())
                    .bakeryPicture(bestReview.getBakeryPicture())
                    .isHACCP(bestReview.getIsHACCP())
                    .isVegan(bestReview.getIsVegan())
                    .isNonGMO(bestReview.getIsNonGMO())
                    .firstNearStation(bestReview.getFirstNearStation())
                    .secondNearStation(bestReview.getSecondNearStation())
                    .isBookMarked(isBookMarked)
                    .bookMarkCount(bestReview.getBookMarkCount())
                    .reviewCount(bestReview.getReviewCount())
                    .reviewText(bestReview.getReviewText())
                    .firstMaxRecommendKeyword(recommendKeywords.get(0))
                    .secondMaxRecommendKeyword(recommendKeywords.get(1))
                    .build();

            responseDtoList.add(response);
        }
        return responseDtoList;
    }

    private Boolean isBookMarked(Long memberId, Long bakeryId) {
        if (bookMarkRepository.findByMemberIdAndBakeryId(memberId, bakeryId).isPresent()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private List<String> getMaxRecommendKeywords(BestReviewDTO bestReview) {

        Map<String, Long> recommendKeywordsMap = new HashMap<>();
        recommendKeywordsMap.put(com.org.gunbbang.RecommendKeyword.DELICIOUS.getMessage(), bestReview.getKeywordDeliciousCount());
        recommendKeywordsMap.put(com.org.gunbbang.RecommendKeyword.KIND.getMessage(), bestReview.getKeywordKindCount());
        recommendKeywordsMap.put(com.org.gunbbang.RecommendKeyword.SPECIAL_MENU.getMessage(), bestReview.getKeywordSpecialCount());
        recommendKeywordsMap.put(com.org.gunbbang.RecommendKeyword.ZERO_WASTE.getMessage(), bestReview.getKeywordZeroWasteCount());

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

        return Arrays.asList(maxKey, secondMaxKey);
    }
}
