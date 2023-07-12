package com.org.gunbbang.service;

import com.org.gunbbang.BadRequestException;
import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.controller.DTO.request.RecommendKeywordNameRequestDto;
import com.org.gunbbang.controller.DTO.request.ReviewRequestDto;
import com.org.gunbbang.controller.DTO.response.RecommendKeywordResponseDto;
import com.org.gunbbang.controller.DTO.response.ReviewListResponseDto;
import com.org.gunbbang.controller.DTO.response.ReviewResponseDto;
import com.org.gunbbang.entity.*;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.*;
import com.org.gunbbang.util.Security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    public Long createReview(Long bakeryId, ReviewRequestDto reviewRequestDto){
        Long currentMemberId = SecurityUtil.getLoginMemberId();
        Member member = memberRepository.findById(currentMemberId).orElseThrow(()->new BadRequestException(ErrorType.TOKEN_TIME_EXPIRED_EXCEPTION));
        Bakery bakery = bakeryRepository.findById(bakeryId).orElseThrow(()->new NotFoundException(ErrorType.NOT_FOUND_BAKERY_EXCEPTION));
        Review review = reviewRepository.save(Review.builder()
                        .memberId(member)
                        .bakeryId(bakery)
                        .isLike(reviewRequestDto.getIsLike())
                        .reviewText(reviewRequestDto.getReviewText())
                .build());
        // 리뷰 작성되면 review count 증가
        bakery.reviewCountChange(true);
        bakeryRepository.save(bakery);
        return review.getReviewId();
    }

    public void createReviewRecommendKeyword(List<RecommendKeywordNameRequestDto> keywordNameRequestDtoList, Long reviewId){
        Review review = reviewRepository.findById(reviewId).orElseThrow(()->new NotFoundException(ErrorType.NOT_FOUND_REVIEW));
        Bakery bakery = bakeryRepository.findById(review.getBakery().getBakeryId()).orElseThrow(()->new NotFoundException(ErrorType.NOT_FOUND_BAKERY_EXCEPTION));
        for(RecommendKeywordNameRequestDto keyword : keywordNameRequestDtoList){
            RecommendKeyword recommendKeyword = recommendKeywordRepository.findByKeywordName(keyword.getKeywordName()).orElseThrow(()->new NotFoundException(ErrorType.NOT_FOUND_CATEGORY_EXCEPTION));
            reviewRecommendKeywordRepository.save(ReviewRecommendKeyword.builder()
                            .recommendKeyword(recommendKeyword)
                            .review(review)
                    .build());
            // 키워드 증가하면 리뷰에도 keywordcount 증가
            bakery.keywordCountChange(keyword.getKeywordName());
        }
        bakeryRepository.save(bakery);
    }

    public ReviewListResponseDto getBakeryReviewList(Long bakeryId){
        Bakery bakery = bakeryRepository.findById(bakeryId).orElseThrow(()->new NotFoundException(ErrorType.NOT_FOUND_BAKERY_EXCEPTION));
        List<Review> reviewList = reviewRepository.findAllByBakeryOrderByCreatedAtDesc(bakery);
        List<ReviewResponseDto> reviewListDto = new ArrayList<>();
        Float tastePercent;
        Float specialPercent;
        Float kindPercent;
        Float zeroPercent;

        for(Review review : reviewList){
            List<RecommendKeywordResponseDto> recommendKeywordList = new ArrayList<>();
            if(review.getIsLike()) {
                List<ReviewRecommendKeyword> reviewRecommendKeywordList = reviewRecommendKeywordRepository.findAllByReview(review);
                for(ReviewRecommendKeyword reviewRecommendKeyword : reviewRecommendKeywordList){
                    recommendKeywordList.add(RecommendKeywordResponseDto.builder()
                                    .recommendKeywordId(reviewRecommendKeyword.getRecommendKeyword().getRecommendKeywordId())
                                    .recommendKeywordName(reviewRecommendKeyword.getRecommendKeyword().getKeywordName())
                            .build());
                }
            }
            reviewListDto.add(ReviewResponseDto.builder()
                    .reviewId(review.getReviewId())
                    .memberNickname(review.getMember().getNickname())
                    .recommendKeywordList(recommendKeywordList)
                    .reviewText(review.getReviewText())
                    .createdAt(review.getCreatedAt().format(DateTimeFormatter.ofPattern("yy.MM.dd")))
                    .build());
        }

        if(bakery.getReviewCount()==0){
            tastePercent = 0f;
            specialPercent = 0f;
            kindPercent = 0f;
            zeroPercent = 0f;
        }
        else{
            tastePercent = bakery.getKeywordDeliciousCount().floatValue()/bakery.getReviewCount().floatValue();
            specialPercent = bakery.getKeywordSpecialCount().floatValue()/bakery.getReviewCount().floatValue();
            kindPercent = bakery.getKeywordKindCount().floatValue()/bakery.getReviewCount().floatValue();
            zeroPercent = bakery.getKeywordZeroWasteCount().floatValue()/bakery.getReviewCount().floatValue();
        }

        return ReviewListResponseDto.builder()
                .tastePercent(tastePercent)
                .specialPercent(specialPercent)
                .kindPercent(kindPercent)
                .zeroPercent(zeroPercent)
                .reviewList(reviewListDto)
                .build();
    }

}
