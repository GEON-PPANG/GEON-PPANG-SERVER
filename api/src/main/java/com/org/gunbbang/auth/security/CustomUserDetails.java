package com.org.gunbbang.auth.security;

import com.org.gunbbang.MainPurpose;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/** UserDetails을 커스텀한 클래스 */
public class CustomUserDetails implements UserDetails {
  private String username; // email
  private Long memberId;
  private String password;
  private boolean enabled = true;
  private MainPurpose mainPurpose;
  private Long breadTypeId;
  private Long nutrientTypeId;
  private String nickName;
  private String role;

  public CustomUserDetails(
      String username,
      String password,
      Long memberId,
      MainPurpose mainPurpose,
      Long breadTypeId,
      Long nutrientTypeId,
      String nickname,
      String role) {
    this.username = username;
    this.password = password;
    this.memberId = memberId;
    this.mainPurpose = mainPurpose;
    this.breadTypeId = breadTypeId;
    this.nutrientTypeId = nutrientTypeId;
    this.nickName = nickname;
    this.role = role;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> auth = new ArrayList<>();
    auth.add(new SimpleGrantedAuthority(this.role));
    return auth;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public Long getMemberId() {
    return memberId;
  }

  public MainPurpose getMainPurpose() {
    return mainPurpose;
  }

  public Long getBreadTypeId() {
    return breadTypeId;
  }

  public Long getNutrientTypeId() {
    return nutrientTypeId;
  }

  public String getNickname() {
    return nickName;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
