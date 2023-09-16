package com.org.gunbbang.repository;

import com.org.gunbbang.entity.BreadType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BreadTypeRepository extends JpaRepository<BreadType, Long> {

  // TODO: getDefaultBreadType으로 수정
  Optional<BreadType> findBreadTypeByIsGlutenFreeAndIsVeganAndIsNutFreeAndIsSugarFree(
      boolean isGlutenFree, boolean isVegan, boolean isNutFree, boolean isSugarFree);

  Optional<BreadType> findByBreadTypeName(String breadTypeName);
}
