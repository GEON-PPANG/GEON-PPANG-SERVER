package com.org.gunbbang.repository;

import com.org.gunbbang.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findById(Long memberId);

  Optional<Member> findByEmail(String email);

  Optional<Member> findByNickname(String nickname);

  Optional<Member> findByRefreshToken(String refreshToken);
}
