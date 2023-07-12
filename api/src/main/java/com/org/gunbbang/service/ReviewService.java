package com.org.gunbbang.service;

import com.org.gunbbang.BadRequestException;
import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.controller.DTO.MemberSignUpRequestDTO;
import com.org.gunbbang.controller.DTO.MemberSignUpResponseDTO;
import com.org.gunbbang.controller.DTO.request.KeywordNameRequestDto;
import com.org.gunbbang.controller.DTO.request.ReviewRequestDto;
import com.org.gunbbang.entity.*;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.*;
import com.org.gunbbang.util.Security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        System.out.println(currentMemberId);
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
        System.out.println(bakery.getBakeryName());
        return review.getReviewId();
    }

    public void createReviewRecommendKeyword(List<KeywordNameRequestDto> keywordNameRequestDtoList, Long reviewId){
        Review review = reviewRepository.findById(reviewId).orElseThrow(()->new NotFoundException(ErrorType.NOT_FOUND_REVIEW));
        Bakery bakery = bakeryRepository.findById(review.getBakeryId().getBakeryId()).orElseThrow(()->new NotFoundException(ErrorType.NOT_FOUND_BAKERY_EXCEPTION));
        for(KeywordNameRequestDto keyword : keywordNameRequestDtoList){
            RecommendKeyword recommendKeyword = recommendKeywordRepository.findByKeywordName(keyword.getKeywordName()).orElseThrow(()->new NotFoundException(ErrorType.NOT_FOUND_CATEGORY_EXCEPTION));
            reviewRecommendKeywordRepository.save(ReviewRecommendKeyword.builder()
                            .recommendKeywordId(recommendKeyword)
                            .reviewId(review)
                    .build());
            // 키워드 증가하면 리뷰에도 keywordcount 증가
            bakery.keywordCountChange(keyword.getKeywordName());
        }
        bakeryRepository.save(bakery);
    }


}
