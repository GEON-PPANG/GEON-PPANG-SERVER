package com.org.gunbbang.auth.security.service;

import com.org.gunbbang.PlatformType;
import com.org.gunbbang.auth.security.CustomUserDetails;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** DaoAuthenticationProvider로 인증처리를 하기 위해 필요한 커스텀 UserDetailsService 클래스 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    Member member =
        memberRepository
            .findByEmailAndPlatformType(userName, PlatformType.NONE)
            .orElseThrow(
                () -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다. 이메일: " + userName));

    CustomUserDetails userDetailsUser =
        new CustomUserDetails(
            member.getEmail(),
            member.getPassword(),
            member.getMemberId(),
            member.getMainPurpose(),
            member.getNickname(),
            member.getRole().toString());

    return userDetailsUser;
  }
}
