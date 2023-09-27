package com.org.gunbbang.jwt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.org.gunbbang.AmplitudeFeignClient;
import com.org.gunbbang.DTO.*;
import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.PlatformType;
import com.org.gunbbang.entity.BookMark;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.entity.Review;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.BookMarkRepository;
import com.org.gunbbang.repository.MemberRepository;
import com.org.gunbbang.repository.ReviewRepository;
import java.lang.reflect.Field;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AmplitudeService {

  @Value("${amplitude.jwt.key}")
  private String amplitudeKey;

  @Value("${amplitude.api.key}")
  private String apiKey;

  @Value("${amplitude.secret.key}")
  private String secretKey;

  private final AmplitudeFeignClient amplitudeFeignClient;
  private final ObjectMapper objectMapper;
  private final MemberRepository memberRepository;
  private final ReviewRepository reviewRepository;
  private final BookMarkRepository bookMarkRepository;

  public String createAmplitudeToken() {
    String valueToEncode = apiKey + ":" + secretKey;
    return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
  }

  // http v2
  public void uploadAuthProperty(PlatformType platformType, String userId) {
    UserPropertyVO userProperty = UserPropertyVO.builder().auth_type(platformType.name()).build();

    HttpV2RequestDTO request = HttpV2RequestDTO.builder().api_key(apiKey).build();

    request.setEvents(userId, "complete_signup", userProperty);
    log.info("HttpV2RequestDTO: " + request);
    amplitudeFeignClient.uploadRequest(request);
  }

  // identify
  public void sendUserAuthProperty(PlatformType platformType, Long memberId) {
    Map<String, Object> propertyMap = new HashMap<>();
    propertyMap.put("auth_type", platformType.name());

    ObjectNode identification = getIdentification(propertyMap, memberId);

    String requestBody = "api_key=" + apiKey + "&identification=" + identification;
    amplitudeFeignClient.identifyUserProperty(requestBody);
  }

  private ObjectNode getIdentification(Map<String, Object> propertyMap, Long memberId) {
    // Create an ObjectNode to build the JSON structure
    ObjectNode identification = objectMapper.createObjectNode();
    identification.put("user_id", memberId.toString());

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

  public void uploadReviewProperty(int reviewCount, String eventType, String userId) {
    UserPropertyVO userProperty = UserPropertyVO.builder().total_review(reviewCount).build();

    HttpV2RequestDTO request = HttpV2RequestDTO.builder().api_key(apiKey).build();

    request.setEvents(userId, eventType, userProperty);
    log.info("HttpV2RequestDTO: " + request);
    amplitudeFeignClient.uploadRequest(request);
  }

  public void sendUserReviewProperty(int reviewCount, Long memberId) {
    Map<String, Object> propertyMap = new HashMap<>();
    propertyMap.put("total_review", Integer.toString(reviewCount));

    ObjectNode identification = getIdentification(propertyMap, memberId);

    String requestBody = "api_key=" + apiKey + "&identification=" + identification;
    amplitudeFeignClient.identifyUserProperty(requestBody);
  }

  // Http v2 api
  public void uploadUserPropertyV2(String memberId, String eventType, Member member) {
    HttpV2RequestDTO request = HttpV2RequestDTO.builder().api_key(apiKey).build();
    request.setEvents(
        memberId + memberId + memberId + memberId + memberId,
        eventType,
        getUserPropertyVO(Long.parseLong(memberId), member));
    log.info("HttpV2RequestDTO: " + request);
    amplitudeFeignClient.uploadRequest(request);
  }

  // identify
  public void sendUserPropertyV2(Long memberId, Member member) throws IllegalAccessException {
    UserPropertyVO vo = getUserPropertyVO(memberId, member);

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
  }

  private UserPropertyVO getUserPropertyVO(Long memberId, Member member) {
    Member foundMember = member;
    if (foundMember == null) {
      foundMember =
          memberRepository
              .findById(memberId)
              .orElseThrow(
                  () ->
                      new NotFoundException(
                          ErrorType.NOT_FOUND_USER_EXCEPTION,
                          ErrorType.NOT_FOUND_USER_EXCEPTION.getMessage() + memberId));
    }
    List<Review> reviews = reviewRepository.findAllByMemberOrderByCreatedAtDesc(foundMember);
    List<BookMark> bookMarks = bookMarkRepository.findAllByMemberId(foundMember.getMemberId());

    System.out.println("review size: " + reviews.size());
    System.out.println("bookMarks size: " + bookMarks.size());

    return UserPropertyVO.builder()
        .auth_type(foundMember.getPlatformType().name())
        //            .account_creation_date(member.getCreatedAt())
        .main_purpose(foundMember.getMainPurpose().name())
        .ingredients_type(foundMember.getNutrientType().getNutrientTypeName())
        .bread_type(foundMember.getBreadType().getBreadTypeName())
        .total_review(reviews.size())
        .total_mystore(bookMarks.size())
        .user_nickname(foundMember.getNickname())
        .build();
  }
}
