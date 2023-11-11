package com.org.gunbbang.entity;

import com.org.gunbbang.BreadTypeTag;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
  private BreadTypeTag breadTypeTag;
}
