package com.org.gunbbang.login;

import com.org.gunbbang.MainPurpose;
import com.org.gunbbang.Role;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
  private Long memberId;
  private String email;
  private MainPurpose mainPurpose;
  private Long breadTypeId;
  private Long nutrientTypeId;
  private Role role;

  public CustomOAuth2User(
      Collection<? extends GrantedAuthority> authorities,
      Map<String, Object> attributes,
      String nameAttributeKey,
      Long memberId,
      String email,
      MainPurpose mainPurpose,
      Long breadTypeId,
      Long nutrientTypeId,
      Role role) {
    super(authorities, attributes, nameAttributeKey);
    this.memberId = memberId;
    this.email = email;
    this.mainPurpose = mainPurpose;
    this.breadTypeId = breadTypeId;
    this.nutrientTypeId = nutrientTypeId;
    this.role = role;
  }

  @Override
  public String toString() {
    return "CustomOAuth2User{" + "email='" + email + '\'' + ", role=" + role + '}';
  }
}
