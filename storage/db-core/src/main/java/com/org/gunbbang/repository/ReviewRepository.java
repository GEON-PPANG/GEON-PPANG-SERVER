package com.org.gunbbang.repository;

import com.org.gunbbang.DTO.BestReviewDTO;
import com.org.gunbbang.MainPurpose;
import com.org.gunbbang.entity.*;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  Optional<Review> findByReviewId(Long reviewId);

  List<Review> findAllByBakeryOrderByCreatedAtDesc(Bakery bakery);

  List<Review> findAllByMemberOrderByCreatedAtDesc(Member member);

  @Query(
      value =
          "SELECT r FROM Review r "
              + "INNER JOIN Bakery b ON b.bakeryId = r.bakery.bakeryId "
              + "INNER JOIN Member m ON r.member.memberId = m.memberId "
              + "WHERE b.breadType = :currentMemberBreadType "
              + "AND m.mainPurpose = :currentMemberMainPurpose "
              + "ORDER BY r.createdAt desc")
  List<Review> findBestReviews(
      @Param("currentMemberBreadType") BreadType currentMemberBreadType,
      @Param("currentMemberMainPurpose") MainPurpose mainPurpose);

  // 나와 같은 목적을 선택하고 같은 빵 유형을 선택한 다른 유저들이 작성한 리뷰 중 10개의 좋아요 리뷰 최신순
  @Query(
      value =
          "SELECT distinct new com.org.gunbbang.DTO.BestReviewDTO("
              + "b.bakeryId, "
              + "b.bakeryName, "
              + "b.bakeryPicture, "
              + "b.isHACCP, "
              + "b.isVegan, "
              + "b.isNonGMO, "
              + "b.firstNearStation, "
              + "b.secondNearStation, "
              + "b.bookMarkCount, "
              + "r.reviewId, "
              + "b.reviewCount, "
              + "r.reviewText, "
              + "b.keywordDeliciousCount, "
              + "b.keywordKindCount, "
              + "b.keywordSpecialCount, "
              + "b.keywordZeroWasteCount, "
              + "r.createdAt) FROM Review r "
              + "INNER JOIN Bakery b ON b.bakeryId = r.bakery.bakeryId "
              + "INNER JOIN Member m ON r.member.memberId = m.memberId "
              + "INNER JOIN MemberBreadType  mbt ON r.member.memberId = mbt.member.memberId "
              + "WHERE mbt.breadType in :currentMemberBreadType "
              + "AND r.isLike = true "
              + "AND r.member.mainPurpose = :currentMemberMainPurpose "
              + "AND r.reviewText IS NOT NULL AND r.reviewText != '' "
              + "ORDER BY r.createdAt desc")
  List<BestReviewDTO> findBestReviewDTOList(
      @Param("currentMemberBreadType") List<BreadType> currentMemberBreadType,
      @Param("currentMemberMainPurpose") MainPurpose mainPurpose,
      PageRequest bestPageRequest);

  @Query(
      value =
          "SELECT distinct new com.org.gunbbang.DTO.BestReviewDTO("
              + "b.bakeryId, "
              + "b.bakeryName, "
              + "b.bakeryPicture, "
              + "b.isHACCP, "
              + "b.isVegan, "
              + "b.isNonGMO, "
              + "b.firstNearStation, "
              + "b.secondNearStation, "
              + "b.bookMarkCount, "
              + "r.reviewId, "
              + "b.reviewCount, "
              + "r.reviewText, "
              + "b.keywordDeliciousCount, "
              + "b.keywordKindCount, "
              + "b.keywordSpecialCount, "
              + "b.keywordZeroWasteCount, "
              + "r.createdAt) FROM Review r "
              + "INNER JOIN Bakery b ON b.bakeryId = r.bakery.bakeryId "
              + "INNER JOIN Member m ON r.member.memberId = m.memberId "
              + "AND r.isLike = true "
              + "WHERE r.reviewId not in :alreadyFoundReviews "
              + "AND r.reviewText IS NOT NULL AND r.reviewText != '' "
              + "ORDER BY r.createdAt desc")
  List<BestReviewDTO> findRestBestReviewDTOListByBreadType(
      @Param("alreadyFoundReviews") List<Long> alreadyFoundReviews, PageRequest pageRequest);

  @Query(
      "SELECT r FROM Review as r "
          + "WHERE r.isLike = true "
          + "AND r.reviewText IS NOT NULL AND r.reviewText != '' "
          + "ORDER BY RAND() ")
  List<Review> findRandomReviews(PageRequest pageRequest);
}
