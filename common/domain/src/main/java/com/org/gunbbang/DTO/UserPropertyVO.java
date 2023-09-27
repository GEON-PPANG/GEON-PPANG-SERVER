package com.org.gunbbang.DTO;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserPropertyVO {
  String auth_type;
  //  LocalDateTime account_creation_date;
  String main_purpose;
  String bread_type;
  String ingredients_type;
  int total_review;
  int total_mystore;
  String user_nickname;
  //  LocalDateTime recent_open_app_date;
}
