package com.org.gunbbang.repository;

import com.org.gunbbang.MainPurpose;
import com.org.gunbbang.entity.Bakery;
import com.org.gunbbang.entity.BreadType;
import com.org.gunbbang.entity.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
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
              + "WHERE m.breadType.breadTypeId = :currentMemberBreadType "
              + "AND m.mainPurpose = :currentMemberMainPurpose "
              + "ORDER BY b.bookMarkCount DESC ")
  List<Bakery> findBestBakeries(
      @Param("currentMemberBreadType") Long currentMemberBreadType,
      @Param("currentMemberMainPurpose") MainPurpose currentMemberMainPurpose,
      Pageable pageRequest);

  @Query(
      value =
          "SELECT b FROM Bakery b "
              + "WHERE b.breadType = :breadTypeId "
              + "AND b.bakeryId not in :alreadyFoundBakeryIds ")
  List<Bakery> findRestBakeriesByBreadTypeId(
      @Param("breadTypeId") BreadType breadTypeId,
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
  List<Bakery> findBakeriesRandomly(PageRequest pageRequest);

  @Query(
      value =
          "SELECT DISTINCT b FROM Bakery b "
              + "INNER JOIN BakeryCategory bc ON b.bakeryId = bc.bakery.bakeryId "
              + "INNER JOIN Category c ON bc.category.categoryId = c.categoryId "
              + "WHERE c IN :categoryList "
              + "ORDER BY b.reviewCount DESC")
  List<Bakery> findBakeriesByCategoryAndReview(List<Category> categoryList);

  @Query(
      value =
          "SELECT DISTINCT b FROM Bakery b "
              + "INNER JOIN BakeryCategory bc ON b.bakeryId = bc.bakery.bakeryId "
              + "INNER JOIN Category c ON bc.category.categoryId = c.categoryId "
              + "WHERE c IN :categoryList "
              + "ORDER BY b.bakeryId DESC")
  List<Bakery> findBakeriesByCategory(List<Category> categoryList);
}
