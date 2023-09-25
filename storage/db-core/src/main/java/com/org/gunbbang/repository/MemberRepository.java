package com.org.gunbbang.repository;

import com.org.gunbbang.PlatformType;
import com.org.gunbbang.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findById(Long memberId);

  Optional<Member> findByEmail(String email);

  List<Member> findAllByEmail(String email);

  Optional<Member> findByEmailAndPlatformType(String email, PlatformType platformType);

  Optional<Member> findByNickname(String nickname);

  Optional<Long> deleteMemberByMemberId(Long memberId);
}
