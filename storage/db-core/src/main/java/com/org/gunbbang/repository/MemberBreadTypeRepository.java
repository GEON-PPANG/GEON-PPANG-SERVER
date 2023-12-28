package com.org.gunbbang.repository;

import com.org.gunbbang.entity.Member;
import com.org.gunbbang.entity.MemberBreadType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberBreadTypeRepository extends JpaRepository<MemberBreadType, Long> {
  List<MemberBreadType> findAllByMember(Member member);

  boolean existsByMember(Member member);

  // List<MemberBreadType> findAllByMemberId(Long memberId);

  // TODO: 쿼리 확인 필요(+transactional 필요없는지)
  @Modifying
  @Query("delete from MemberBreadType mbt where mbt.member =: member")
  void deleteAllByMember(Member member);
}
