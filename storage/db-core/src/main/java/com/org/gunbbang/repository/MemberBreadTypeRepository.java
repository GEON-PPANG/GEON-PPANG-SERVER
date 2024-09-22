package com.org.gunbbang.repository;

import com.org.gunbbang.entity.Member;
import com.org.gunbbang.entity.MemberBreadType;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberBreadTypeRepository extends JpaRepository<MemberBreadType, Long> {
  List<MemberBreadType> findAllByMember(Member member);

  @Query("SELECT mbt FROM MemberBreadType mbt where mbt.member.memberId = :memberId")
  @Transactional
  List<MemberBreadType> findAllByMemberId(Long memberId);

  boolean existsByMember(Member member);

  // List<MemberBreadType> findAllByMemberId(Long memberId);

  @Modifying
  @Query("delete from MemberBreadType mbt where mbt.member = :member")
  @Transactional
  void deleteAllByMember(Member member);
}
