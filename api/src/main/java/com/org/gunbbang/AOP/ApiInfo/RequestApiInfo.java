package com.org.gunbbang.AOP.ApiInfo;

import com.org.gunbbang.auth.jwt.service.JwtService;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Getter
@Slf4j
@RequiredArgsConstructor
public class RequestApiInfo {

  private Long memberId = null;
  private String method = null;
  private String url = null;
  private String name = null;
  private Map<String, Object> parameters = new HashMap<>();
  private Map<String, String> pathVariables = new HashMap<>();
  private Map<String, Object> body = new HashMap<>();
  private LocalDateTime logTimeStamp;
  private final JwtService jwtService;

  public RequestApiInfo(JoinPoint joinPoint, Class clazz, JwtService jwtService) {
    this.jwtService = jwtService;
    try {
      final HttpServletRequest request =
          ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
      setLogTimestamp();
      setApiInfo(joinPoint, clazz);
      setBodyAndParameter(joinPoint);
      setMemberId(request);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setLogTimestamp() {
    LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    logTimeStamp = dateTime;
  }

  private void setMemberId(HttpServletRequest request) {
    final Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      final String headerName = headerNames.nextElement();
      if (headerName.equals("authorization")) {
        String accessToken = jwtService.extractAccessTokenAsString(request);
        this.memberId = jwtService.extractMemberIdClaim(accessToken);
      }
    }
  }

  // Api 정보 추출
  private void setApiInfo(JoinPoint joinPoint, Class clazz) {
    final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    if (methodSignature != null) {
      final Method method = methodSignature.getMethod(); // methodSignature에서 실행 중인 메서드를 가져옴
      if (method != null) {
        final Class mappingClass = getMappingClass(method);
        final Annotation annotation = method.getAnnotation(mappingClass);

        setMethod(mappingClass);
        setName(mappingClass, annotation);
        setFullUrl(clazz, mappingClass, annotation);
      }
    }
  }

  private static Class<? extends Annotation> getMappingClass(Method method) {
    return Stream.of(
            GetMapping.class,
            PostMapping.class,
            DeleteMapping.class,
            RequestMapping.class,
            PutMapping.class,
            PatchMapping.class)
        .filter(method::isAnnotationPresent) // 위 스트림에 넣은 어노테이션 중 method에 하나라도 있으면 그걸 가져옴
        .findFirst()
        .get();
  }

  private void setMethod(Class mappingClass) {
    this.method = (mappingClass.getSimpleName().replace("Mapping", "")).toUpperCase();
  }

  private void setName(Class mappingClass, Annotation annotation) {
    try {
      this.name = (String) mappingClass.getMethod("name").invoke(annotation);
    } catch (Exception e) {
      log.warn("@@@@@@@@@@ AOP에서 api name 추출할 때 에러 발생 @@@@@@@@@@");
      e.printStackTrace();
    }
  }

  private void setFullUrl(Class clazz, Class mappingClass, Annotation annotation) {
    String baseUrl = getBaseUrl(clazz);
    try {
      final String[] methodUrl = (String[]) mappingClass.getMethod("value").invoke(annotation);
      this.url = String.format("%s%s", baseUrl, methodUrl.length > 0 ? methodUrl[0] : "");
    } catch (Exception e) {
      log.warn("@@@@@@@@@@ AOP에서 full url 추출할 때 에러 발생 @@@@@@@@@@");
      e.printStackTrace();
    }
  }

  private String getBaseUrl(Class clazz) {
    // clazz에서 RequestMapping 어노테이션을 가져와 requestMapping 변수에 저장
    final RequestMapping requestMapping =
        (RequestMapping) clazz.getAnnotation(RequestMapping.class);

    String baseUrl = "";
    if (requestMapping != null && requestMapping.value().length != 0) {
      baseUrl = requestMapping.value()[0]; // @RequestMapping("/이 값을 baseUrl에 할당")
    }
    return baseUrl;
  }

  private void setBodyAndParameter(JoinPoint joinPoint) {
    try {
      final CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
      final String[] parameterNames = codeSignature.getParameterNames();
      final Object[] args = joinPoint.getArgs();
      Map<String, Object> parametersMap = new HashMap<>();
      for (int i = 0; i < parameterNames.length; i++) {
        if (parameterNames[i].equals("request")) {
          this.body = convertRequestBodyToMap(args[i]);
        } else {
          if (args[i] != null && !parameterNames[i].equals("response")) {
            parametersMap.put(parameterNames[i], args[i]);
          }
        }
      }
      this.parameters = parametersMap;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Map<String, Object> convertRequestBodyToMap(Object obj) {
    Map<String, Object> requestBodyMap = new HashMap<>();

    // 클래스의 필드 배열 가져오기
    Field[] fields = obj.getClass().getDeclaredFields();

    for (Field field : fields) {
      try {
        String fieldName = field.getName();
        if (fieldName.equals("password")) {
          continue;
        }
        field.setAccessible(true);
        requestBodyMap.put(fieldName, field.get(obj));
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }

    System.out.println(requestBodyMap);

    return requestBodyMap;
  }
}
