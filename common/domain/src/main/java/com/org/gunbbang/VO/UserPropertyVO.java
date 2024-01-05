package com.org.gunbbang.VO;

import com.org.gunbbang.BreadTypeTag;
import com.org.gunbbang.NutrientTypeTag;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserPropertyVO {
  String auth_type;
  LocalDateTime account_creation_date;
  String main_purpose;
  List<BreadTypeTag> bread_type_tags;
  List<NutrientTypeTag> nutrient_type_tags;
  int total_review;
  int total_mystore;
  String user_nickname;
}
