package com.org.gunbbang.repository;

import com.org.gunbbang.entity.Bakery;
import com.org.gunbbang.entity.BakeryBreadType;
import com.org.gunbbang.entity.BreadType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BakeryBreadTypeRepository extends JpaRepository<BakeryBreadType, Long> {
  List<BakeryBreadType> findAllByBreadType(BreadType breadType);

  List<BakeryBreadType> findAllByBakery(Bakery bakery);
}
