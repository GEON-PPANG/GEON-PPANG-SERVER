package com.org.gunbbang.repository;

import com.org.gunbbang.entity.Member;
import com.org.gunbbang.entity.MemberNutrientType;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberNutrientTypeRepository extends JpaRepository<MemberNutrientType, Long> {
  List<MemberNutrientType> findAllByMember(Member member);

  @Query(
      "select mnt from MemberNutrientType mnt where mnt.member.memberId = :memberId order by mnt.memberNutrientTypeId asc")
  List<MemberNutrientType> findAllByMemberId(Long memberId);

  boolean existsByMember(Member member);

  @Modifying
  @Query("delete from MemberNutrientType mnt where mnt.member=:member")
  @Transactional
  void deleteAllByMember(Member member);

  @Query(
      "select mnt from MemberNutrientType mnt where mnt.member.memberId = :memberId order by mnt.memberNutrientTypeId asc")
  List<MemberNutrientType> findByMemberId(Long memberId);
}
