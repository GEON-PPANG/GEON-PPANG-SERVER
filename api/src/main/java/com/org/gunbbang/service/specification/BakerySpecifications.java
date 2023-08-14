package com.org.gunbbang.service.specification;

import com.org.gunbbang.entity.Bakery;
import org.springframework.data.jpa.domain.Specification;

public class BakerySpecifications {
  public static Specification<Bakery> searchBakery(String keyword) {
    return (root, query, criteriaBuilder) -> {
      String likePattern = "%" + keyword + "%";
      return criteriaBuilder.or(
          criteriaBuilder.like(root.get("state"), likePattern),
          criteriaBuilder.like(root.get("city"), likePattern),
          criteriaBuilder.like(root.get("town"), likePattern),
          criteriaBuilder.like(root.get("addressRest"), likePattern),
          criteriaBuilder.like(root.get("firstNearStation"), likePattern),
          criteriaBuilder.like(root.get("secondNearStation"), likePattern),
          criteriaBuilder.like(root.get("bakeryName"), likePattern));
    };
  }
}
