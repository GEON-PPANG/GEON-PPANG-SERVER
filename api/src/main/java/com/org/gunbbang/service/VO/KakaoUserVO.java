package com.org.gunbbang.service.VO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserVO {
  private Long id;

  @JsonProperty("kakao_account")
  private KakaoAccount kakaoAccount;

  @Getter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class KakaoAccount {
    @NotNull private String email;
    private Boolean age_range_needs_agreement;
    private String age_range;
    private Boolean gender_needs_agreement;
    private String gender;
  }

  @Override
  public String toString() {
    return "KakaoUserVO{"
        + "id="
        + id
        + "age_range_need_agreement="
        + kakaoAccount.getAge_range_needs_agreement()
        + "gender_need_agreement="
        + kakaoAccount.getGender_needs_agreement()
        + '}';
  }
}
