package com.org.gunbbang.entity;

import com.org.gunbbang.BreadTypeTag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BreadType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "bread_type_id")
  private Long breadTypeId;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "bread_type_tag")
  private BreadTypeTag breadTypeTag;
}
