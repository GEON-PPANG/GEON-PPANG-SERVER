package com.org.gunbbang.repository;

import com.org.gunbbang.MainPurpose;
import com.org.gunbbang.entity.Bakery;
import com.org.gunbbang.entity.BreadType;
import com.org.gunbbang.entity.Category;
import java.util.List;
import java.util.Optional;
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
      "SELECT DISTINCT b FROM Bakery b "
          + "INNER JOIN BakeryCategory bc ON b.bakeryId = bc.bakery.bakeryId "
          + "INNER JOIN Category c ON bc.category.categoryId = c.categoryId "
          + "WHERE (c IN :categoryList) "
          + "AND ( "
          + "(:isGlutenFree = true AND b.breadType.isGlutenFree = true) OR "
          + "(:isVegan = true AND b.breadType.isVegan = true) OR "
          + "(:isNutFree = true AND b.breadType.isNutFree = true) OR "
          + "(:isSugarFree = true AND b.breadType.isSugarFree = true)"
          + ")")
  List<Bakery> findFilteredBakeries(
      @Param("categoryList") List<Category> categoryList,
      @Param("isGlutenFree") boolean isGlutenFree,
      @Param("isVegan") boolean isVegan,
      @Param("isNutFree") boolean isNutFree,
      @Param("isSugarFree") boolean isSugarFree);
}
