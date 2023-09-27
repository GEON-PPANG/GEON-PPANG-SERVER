package com.org.gunbbang.DTO;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserPropertyRequestDTO {
  String auth_type;
  LocalDateTime account_creation_date;
  String main_purpose;
  String bread_type;
  String ingredients_type;
  Long total_review;
  Long total_mystore;
  String user_nickname;
  //  LocalDateTime recent_open_app_date;
}
