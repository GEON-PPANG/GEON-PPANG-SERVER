package com.org.gunbbang.service;

import com.org.gunbbang.BadRequestException;
import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.controller.DTO.MemberSignUpRequestDTO;
import com.org.gunbbang.controller.DTO.MemberSignUpResponseDTO;
import com.org.gunbbang.controller.DTO.request.KeywordNameRequestDto;
import com.org.gunbbang.controller.DTO.request.ReviewRequestDto;
import com.org.gunbbang.entity.Bakery;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.entity.RecommendKeyword;
import com.org.gunbbang.entity.Review;
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
        Member member = memberRepository.findById(currentMemberId).orElseThrow(()->new BadRequestException(ErrorType.TOKEN_TIME_EXPIRED_EXCEPTION));
        Bakery bakery = bakeryRepository.findById(bakeryId).orElseThrow(()->new NotFoundException(ErrorType.NOT_FOUND_BAKERY_EXCEPTION));
        Long reviewId = reviewRepository.save(Review.builder()
                        .memberId(member)
                        .bakeryId(bakery)
                        .isLike(reviewRequestDto.getIsLike())
                        .reviewText(reviewRequestDto.getReviewText())
                .build()).orElseThrow(()->new NotFoundException(ErrorType.NOT_FOUND_SAVE_REVIEW)).getReviewId();
        //TODO: 리뷰 작성되면 review count 증가

        return reviewId;
    }

    public void createReviewRecommendKeyword(List<KeywordNameRequestDto> keywordNameRequestDtoList, Long reviewId){
        Review review = reviewRepository.findById(reviewId).orElseThrow(()->new NotFoundException(ErrorType.NOT_FOUND_REVIEW));
        for(KeywordNameRequestDto keyword : keywordNameRequestDtoList){
            RecommendKeyword recommendKeyword = recommendKeywordRepository.findByKeywordName(keyword.getKeywordName()).orElseThrow(new ));
            //TODO: 키워드 증가하면 리뷰에도 keywordcount 증가
        }
    }


}
