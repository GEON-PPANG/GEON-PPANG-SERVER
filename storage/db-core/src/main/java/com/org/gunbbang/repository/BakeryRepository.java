package com.org.gunbbang.repository;

import com.org.gunbbang.MainPurpose;
import com.org.gunbbang.entity.*;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BakeryRepository
    extends JpaRepository<Bakery, Long>, JpaSpecificationExecutor<Bakery> {
  Optional<Bakery> findById(Long Id);

  // 나와 빵유형과 주목적이 일치하는 유저들이 북마크한 빵집중에 가장 북마크수가 높은 순대로 조회
  @Query(
      value =
          "SELECT distinct b FROM Bakery b "
              + "INNER JOIN BookMark bm ON b.bakeryId = bm.bakery.bakeryId "
              + "INNER JOIN Member m ON bm.member.memberId = m.memberId "
              + "INNER JOIN MemberBreadType mbt ON mbt.member.memberId = m.memberId "
              + "WHERE mbt.breadType in :currentMemberBreadType "
              + "AND m.mainPurpose = :currentMemberMainPurpose "
              + "ORDER BY b.bookMarkCount DESC ")
  List<Bakery> findBestBakeries(
      @Param("currentMemberBreadType") List<BreadType> currentMemberBreadType,
      @Param("currentMemberMainPurpose") MainPurpose currentMemberMainPurpose,
      Pageable pageRequest);

  @Query(
      value =
          "SELECT b FROM Bakery b "
              + "INNER JOIN BakeryBreadType bbt ON b.bakeryId = bbt.bakery.bakeryId "
              + "WHERE bbt.breadType in :breadTypes "
              + "AND b.bakeryId not in :alreadyFoundBakeryIds ")
  List<Bakery> findRestBakeriesByBreadTypes(
      @Param("breadTypes") List<BreadType> breadTypes,
      @Param("alreadyFoundBakeryIds") List<Long> alreadyFoundBakeryIds,
      Pageable pageRequest);

  @Query(value = "SELECT b FROM Bakery b " + "WHERE b.bakeryName like %:bakeryName% ")
  List<Bakery> findBakeryByBakeryName(@Param("bakeryName") String bakeryName);

  @Query(
      value =
          "SELECT distinct b FROM Bakery b "
              + "INNER JOIN BookMark bm ON b.bakeryId = bm.bakery.bakeryId "
              + "INNER JOIN Member m ON bm.member.memberId = m.memberId "
              + "WHERE m.memberId = :memberId")
  List<Bakery> findBookMarkedBakeries(@Param("memberId") Long memberId);

  @Query(
      value =
          "SELECT b FROM Bakery b "
              + "WHERE b.bakeryId NOT IN :alreadyFoundBakeryIds "
              + "ORDER BY RAND() ")
  List<Bakery> findRestBakeriesRandomly(
      @Param("alreadyFoundBakeryIds") List<Long> alreadyFoundBakeryIds, Pageable pageRequest);

  @Query(value = "SELECT b FROM Bakery b " + "ORDER BY RAND() ")
  List<Bakery> findBakeriesRandomly(Pageable pageRequest);

  @Query(
      value =
          "SELECT b FROM Bakery b "
              + "INNER JOIN BakeryBreadType bbt ON b.bakeryId = bbt.bakery.bakeryId AND bbt.breadType IN :breadTypeList "
              + "INNER JOIN BakeryCategory bc ON b.bakeryId = bc.bakery.bakeryId AND bc.category IN :categoryList "
              + "INNER JOIN BakeryNutrientType bnt ON b.bakeryId = bnt.bakery.bakeryId AND bnt.nutrientType = :bakeryNutrientType "
              + "GROUP BY b.bakeryId "
              + "ORDER BY COUNT(bbt.bakery.bakeryId) DESC, COUNT(bc.bakery.bakeryId) DESC")
  Page<Bakery> findFilteredBakeries(
      @Param("categoryList") List<Category> categoryList,
      @Param("breadTypeList") List<BreadType> breadTypeList,
      NutrientType bakeryNutrientType,
      Pageable pageable);

  @Query(
      value =
          "SELECT distinct b FROM Bakery b "
              + "INNER JOIN BakeryBreadType bbt ON b.bakeryId = bbt.bakery.bakeryId AND bbt.breadType IN :breadTypeList "
              + "INNER JOIN BakeryCategory bc ON b.bakeryId = bc.bakery.bakeryId AND bc.category IN :categoryList "
              + "INNER JOIN BakeryNutrientType bnt ON b.bakeryId = bnt.bakery.bakeryId AND bnt.nutrientType = :bakeryNutrientType "
              + "GROUP BY b.bakeryId "
              + "ORDER BY b.reviewCount DESC, COUNT(bbt.bakery.bakeryId) DESC, COUNT(bc.bakery.bakeryId) DESC")
  Page<Bakery> findFilteredBakeriesSortByReview(
      @Param("categoryList") List<Category> categoryList,
      @Param("breadTypeList") List<BreadType> breadTypeList,
      NutrientType bakeryNutrientType,
      Pageable pageable);
}
