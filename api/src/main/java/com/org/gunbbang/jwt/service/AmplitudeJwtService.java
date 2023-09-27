package com.org.gunbbang.jwt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.org.gunbbang.AmplitudeFeignClient;
import com.org.gunbbang.DTO.*;
import com.org.gunbbang.PlatformType;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AmplitudeJwtService {

  @Value("${amplitude.jwt.key}")
  private String amplitudeKey;

  @Value("${amplitude.api.key}")
  private String apiKey;

  @Value("${amplitude.secret.key}")
  private String secretKey;

  private final AmplitudeFeignClient amplitudeFeignClient;
  private final ObjectMapper objectMapper;

  public String createAmplitudeToken() {
    String valueToEncode = apiKey + ":" + secretKey;
    return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
  }

  // http v2
  public void uploadAuthProperty(PlatformType platformType, String userId) {
    UserPropertyRequestDTO userProperty =
        UserPropertyRequestDTO.builder().auth_type(platformType.name()).build();

    HttpV2RequestDTO request = HttpV2RequestDTO.builder().api_key(apiKey).build();

    request.setEvents(userId, "complete_signup", userProperty);
    log.info("HttpV2RequestDTO: " + request);
    amplitudeFeignClient.uploadRequest(request);
  }

  // identify
  public void sendUserAuthProperty(PlatformType platformType, String memberId) {
    Map<String, Object> propertyMap = new HashMap<>();
    propertyMap.put("auth_type", platformType.name());

    String identification = getIdentification(propertyMap, memberId);

    String requestBody = "api_key=" + apiKey + "&identification=" + identification;
    amplitudeFeignClient.identifyUserProperty(requestBody);
  }

  private String getIdentification(Map<String, Object> propertyMap, String memberId) {
    // Create an ObjectNode to build the JSON structure
    ObjectNode identification = objectMapper.createObjectNode();
    identification.put("user_id", memberId);

    // Create user_properties object and add it to the root node
    identification.set("user_properties", getUserProperties(propertyMap));

    // Convert the ObjectNode to a JSON string
    return identification.toString();
  }

  private ObjectNode getUserProperties(Map<String, Object> propertyMap) {
    ObjectNode userPropertiesNode = objectMapper.createObjectNode();
    for (String propertyKey : propertyMap.keySet()) {
      userPropertiesNode.put(propertyKey, propertyMap.get(propertyKey).toString());
    }
    return userPropertiesNode;
  }

  public void uploadReviewProperty(long reviewCount, String eventType, String userId) {
    UserPropertyRequestDTO userProperty =
        UserPropertyRequestDTO.builder().total_review(reviewCount).build();

    HttpV2RequestDTO request = HttpV2RequestDTO.builder().api_key(apiKey).build();

    request.setEvents(userId, eventType, userProperty);
    log.info("HttpV2RequestDTO: " + request);
    amplitudeFeignClient.uploadRequest(request);
  }

  public void sendUserReviewProperty(long reviewCount, String memberId) {
    Map<String, Object> propertyMap = new HashMap<>();
    propertyMap.put("total_review", reviewCount);

    String identification = getIdentification(propertyMap, memberId);

    String requestBody = "api_key=" + apiKey + "&identification=" + identification;
    amplitudeFeignClient.identifyUserProperty(requestBody);
  }
}
