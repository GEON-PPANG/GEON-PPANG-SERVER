package com.org.gunbbang.repository;

import com.org.gunbbang.BestReviewDTO;
import com.org.gunbbang.MainPurpose;
import com.org.gunbbang.entity.Bakery;
import com.org.gunbbang.entity.BreadType;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.entity.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  Optional<Review> findById(Long reviewId);

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

  @Query(
      value =
          "SELECT distinct new com.org.gunbbang.BestReviewDTO("
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
              + "WHERE r.member.breadType.breadTypeId = :currentMemberBreadType "
              + "AND r.isLike = true "
              + "AND r.member.mainPurpose = :currentMemberMainPurpose "
              + "ORDER BY r.createdAt desc")
  List<BestReviewDTO> findBestReviewDTOList(
      @Param("currentMemberBreadType") Long currentMemberBreadType,
      @Param("currentMemberMainPurpose") MainPurpose mainPurpose,
      PageRequest bestPageRequest);

  @Query(
      value =
          "SELECT distinct new com.org.gunbbang.BestReviewDTO("
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
              + "ORDER BY r.createdAt desc")
  List<BestReviewDTO> findRestBestReviewDTOListByBreadType(
      @Param("alreadyFoundReviews") List<Long> alreadyFoundReviews, PageRequest pageRequest);
}
