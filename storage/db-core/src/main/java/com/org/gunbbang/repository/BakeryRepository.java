package com.org.gunbbang.repository;

import com.org.gunbbang.MainPurpose;
import com.org.gunbbang.entity.Bakery;
import com.org.gunbbang.entity.BreadType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface BakeryRepository extends JpaRepository<Bakery, Long> {
    Optional<Bakery> findById(Long Id);

    // 나와 빵유형과 주목적이 일치하는 유저들이 북마크한 빵집중에 가장 북마크수가 높은 순대로 조회
    @Query(value = "SELECT distinct b FROM Bakery b " +
            "INNER JOIN BookMark bm ON b.bakeryId = bm.bakery.bakeryId " +
            "INNER JOIN Member m ON bm.member.memberId = m.memberId " +
            "WHERE m.breadType.breadTypeId = :currentMemberBreadType " +
            "AND m.mainPurpose = :currentMemberMainPurpose " +
            "ORDER BY b.bookMarkCount DESC ")
    List<Bakery> findBestBakeries(
            @Param("currentMemberBreadType") Long currentMemberBreadType,
            @Param("currentMemberMainPurpose") MainPurpose currentMemberMainPurpose,
            PageRequest pageRequest
    );

    @Query(value = "SELECT b FROM Bakery b " +
            "WHERE b.breadType = :breadTypeId " +
            "AND b.bakeryId not in :alreadyFoundBakeries " )
    List<Bakery> findRestBakeriesByBreadTypeId(
            BreadType breadTypeId,
            List<Long> alreadyFoundBakeries,
            PageRequest pageRequest);

    @Query(value = "SELECT b FROM Bakery b " +
            "WHERE b.bakeryName like %:bakeryName% ")
    List<Bakery> findBakeryByBakeryName(@Param("bakeryName") String bakeryName);

    @Query(value = "SELECT distinct b FROM Bakery b " +
            "INNER JOIN BookMark bm ON b.bakeryId = bm.bakery.bakeryId " +
            "INNER JOIN Member m ON bm.member.memberId = m.memberId " +
            "WHERE m.memberId = :memberId")
    List<Bakery> findBookMarkedBakeries(@Param("memberId") Long memberId);

    @Query(value = "SELECT b FROM Bakery b " +
            "WHERE b.bakeryId not in :alreadyFoundBakeries " +
            "ORDER BY RAND() ")
    List<Bakery> findRestBakeriesRandomly(List<Long> alreadyFoundBakeries,
                                          PageRequest pageRequest);
}
