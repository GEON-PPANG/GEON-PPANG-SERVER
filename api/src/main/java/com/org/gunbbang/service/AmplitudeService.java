package com.org.gunbbang.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.org.gunbbang.AmplitudeFeignClient;
import com.org.gunbbang.BreadTypeTag;
import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.NutrientTypeTag;
import com.org.gunbbang.VO.UserPropertyVO;
import com.org.gunbbang.entity.*;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AmplitudeService {
  @Value("${amplitude.api.key}")
  private String apiKey;

  @Value("${amplitude.secret.key}")
  private String secretKey;

  private final AmplitudeFeignClient amplitudeFeignClient;
  private final ObjectMapper objectMapper;
  private final MemberRepository memberRepository;
  private final ReviewRepository reviewRepository;
  private final BookMarkRepository bookMarkRepository;
  private final MemberBreadTypeRepository memberBreadTypeRepository;
  private final MemberNutrientTypeRepository memberNutrientTypeRepository;

  public String createAmplitudeToken() {
    String valueToEncode = apiKey + ":" + secretKey;
    return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
  }

  private ObjectNode getIdentification(Map<String, Object> propertyMap, Long memberId) {
    // Create an ObjectNode to build the JSON structure
    ObjectNode identification = objectMapper.createObjectNode();
    identification.put("user_id", getAmplUserId(memberId));

    // Create user_properties object and add it to the root node
    identification.set("user_properties", getUserProperties(propertyMap));

    // Convert the ObjectNode to a JSON string
    return identification;
  }

  private ObjectNode getUserProperties(Map<String, Object> propertyMap) {
    ObjectNode userPropertiesNode = objectMapper.createObjectNode();
    for (String propertyKey : propertyMap.keySet()) {
      userPropertiesNode.put(propertyKey, propertyMap.get(propertyKey).toString());
    }
    return userPropertiesNode;
  }

  // identify
  public void sendUserProperty(Long memberId) {
    try {
      UserPropertyVO vo = getUserPropertyVO(memberId);

      Map<String, Object> propertyMap = new HashMap<>();
      Field[] fields = vo.getClass().getDeclaredFields(); // TODO: 리플렉션 안쓰고 하는 방법은 없을지?
      for (Field field : fields) {
        field.setAccessible(true);

        String propertyKey = field.getName();
        Object propertyValue = field.get(vo);

        propertyMap.put(propertyKey, propertyValue);
      }

      ObjectNode identification = getIdentification(propertyMap, memberId);

      String requestBody = "api_key=" + apiKey + "&identification=" + identification;
      amplitudeFeignClient.identifyUserProperty(requestBody);
    } catch (Exception e) {
      log.error("%%%%%%%%%% user property 전송 과정에서 에러 발생 %%%%%%%%%%");
      e.printStackTrace();
    }
  }

  private UserPropertyVO getUserPropertyVO(Long memberId) {
    Member foundMember =
        memberRepository
            .findById(memberId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorType.NOT_FOUND_USER_EXCEPTION,
                        ErrorType.NOT_FOUND_USER_EXCEPTION.getMessage() + memberId));

    List<Review> reviews = reviewRepository.findAllByMemberOrderByCreatedAtDesc(foundMember);
    List<BookMark> bookMarks = bookMarkRepository.findAllByMemberId(foundMember.getMemberId());
    List<BreadTypeTag> foundBreadTypeTags =
        memberBreadTypeRepository.findAllByMemberId(foundMember.getMemberId()).stream()
            .map(MemberBreadType::getBreadType)
            .map(BreadType::getBreadTypeTag)
            .collect(Collectors.toList());

    List<NutrientTypeTag> foundNutrientTypeTags =
        memberNutrientTypeRepository.findAllByMember(foundMember).stream()
            .map(MemberNutrientType::getNutrientType)
            .map(NutrientType::getNutrientTypeTag)
            .collect(Collectors.toList());

    return UserPropertyVO.builder()
        .auth_type(foundMember.getPlatformType().name())
        .account_creation_date(foundMember.getCreatedAt())
        .main_purpose(foundMember.getMainPurpose().name())
        .bread_type_tags(foundBreadTypeTags)
        .nutrient_type_tags(foundNutrientTypeTags)
        .total_review(reviews.size())
        .total_mystore(bookMarks.size())
        .user_nickname(foundMember.getNickname())
        .build();
  }

  private String getAmplUserId(Long memberId) {
    return "gunbbang" + memberId.toString();
  }
}
