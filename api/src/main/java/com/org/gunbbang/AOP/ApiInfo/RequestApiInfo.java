package com.org.gunbbang.AOP.ApiInfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Getter
public class RequestApiInfo {

  //    private Long memberId = null;
  private String method = null;
  private String url = null;
  private String name = null;
  private Map<String, String> header = new HashMap<>();
  private String parameters = null;
  private Map<String, String> pathVariables = new HashMap<>();
  private String body = null;
  private String ipAddress = null;
  private final String dateTime =
      LocalDateTime.now(ZoneId.of("Asia/Seoul"))
          .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  ;

  public RequestApiInfo(JoinPoint joinPoint, Class clazz, ObjectMapper objectMapper) {

    try {
      final HttpServletRequest request =
          ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
      setHeader(request);
      setIpAddress(request);
      setApiInfo(joinPoint, clazz);
      setInputStream(joinPoint, objectMapper);
      //            setUser();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Request에서 header 추출
  private void setHeader(HttpServletRequest request) {
    final Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      final String headerName = headerNames.nextElement();
      this.header.put(headerName, request.getHeader(headerName));
    }
  }

  // Request에서 ipAdress 추출
  // TODO: 이 코드 개선할 방법은 없을지??
  private void setIpAddress(HttpServletRequest request) {
    this.ipAddress =
        Optional.of(request)
            .map(
                httpServletRequest ->
                    Optional.ofNullable(request.getHeader("X-Forwarded-For"))
                        .orElse(
                            Optional.ofNullable(request.getHeader("Proxy-Client-IP"))
                                .orElse(
                                    Optional.ofNullable(request.getHeader("WL-Proxy-Client-IP"))
                                        .orElse(
                                            Optional.ofNullable(request.getHeader("HTTP_CLIENT_IP"))
                                                .orElse(
                                                    Optional.ofNullable(
                                                            request.getHeader(
                                                                "HTTP_X_FORWARDED_FOR"))
                                                        .orElse(request.getRemoteAddr()))))))
            .orElse(null);
  }

  // SecurityContextHolder에서 회원 id 추출
  //    private void setUser() {
  //        this.memberId = SecurityUtil.getLoginMemberId();
  //    }

  // Api 정보 추출
  private void setApiInfo(JoinPoint joinPoint, Class clazz) {
    final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    if (methodSignature != null) {

      final Method method = methodSignature.getMethod(); // methodSignature에서 실행 중인 메서드를 가져옴

      // clazz에서 RequestMapping 어노테이션을 가져와 requestMapping 변수에 저장
      // TODO: @RequestMapping 어노테이션 안붙은 경우에 requestMapping에 null이 들어감 개선 필요
      final RequestMapping requestMapping =
          (RequestMapping) clazz.getAnnotation(RequestMapping.class);

      String baseUrl;
      if (requestMapping != null && requestMapping.value().length != 0) {
        baseUrl = requestMapping.value()[0]; // @RequestMapping("/이 값을 baseUrl에 할당")
      } else {
        baseUrl = "";
      }

      Stream.of(
              GetMapping.class,
              PutMapping.class,
              PostMapping.class,
              DeleteMapping.class,
              RequestMapping.class,
              PutMapping.class,
              PatchMapping.class)
          .filter(method::isAnnotationPresent) // 위 스트림에 넣은 어노테이션 중 method에 하나라도 있으면 그걸 가져옴
          .findFirst()
          .ifPresent(
              mappingClass -> {
                final Annotation annotation =
                    method.getAnnotation(
                        mappingClass); // 메서드에서 mappingClass에 해당하는 어노테이션을 가져와 annotation 변수에 저장
                try {
                  final String[] methodUrl =
                      (String[]) mappingClass.getMethod("value").invoke(annotation);
                  this.method = (mappingClass.getSimpleName().replace("Mapping", "")).toUpperCase();
                  this.url =
                      String.format("%s%s", baseUrl, methodUrl.length > 0 ? methodUrl[0] : "");
                  this.name = (String) mappingClass.getMethod("name").invoke(annotation);
                } catch (Exception e) {
                  System.out.println("method, url, name 빼낼때 에러");
                  e.printStackTrace();
                }
              });
    }
  }

  private void setInputStream(JoinPoint joinPoint, ObjectMapper objectMapper) {
    try {
      final CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
      final String[] parameterNames = codeSignature.getParameterNames();
      final Object[] args = joinPoint.getArgs();
      for (int i = 0; i < parameterNames.length; i++) {
        if (parameterNames[i].equals("request")) {
          this.body = args[i].toString();
        } else {
          this.parameters = args[i].toString();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
