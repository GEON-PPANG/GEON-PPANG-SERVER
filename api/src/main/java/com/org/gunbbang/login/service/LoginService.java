package com.org.gunbbang.login.service;

import com.org.gunbbang.entity.Member;
import com.org.gunbbang.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Member member =
        memberRepository
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다."));
    return org.springframework.security.core.userdetails.User.builder()
        .username(member.getEmail())
        .password(member.getPassword())
        .roles(member.getRole().name())
        .build();
  }
}
