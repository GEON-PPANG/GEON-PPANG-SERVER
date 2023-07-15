package com.org.gunbbang.AOP;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.gunbbang.util.Security.SecurityUtil;
import lombok.Getter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
public class RequestApiInfo {

//    private Long memberId = null;
//    private String userName = null; // email
    private String method = null;
    private String url = null;
    private String name = null;
    private Map<String, String> header = new HashMap<>();
    private Map<String, String> parameters = new HashMap<>();
    private Map<String, String> body = new HashMap<>();
    private String ipAddress = null;
    private final String dateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));;

    public RequestApiInfo(JoinPoint joinPoint, Class clazz, ObjectMapper objectMapper) {

        try {
            final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            setHeader(request);
            setIpAddress(request);
//            setUser();
            setApiInfo(joinPoint, clazz);
            setInputStream(joinPoint, objectMapper);
        } catch(Exception e) {
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
        this.ipAddress = Optional.of(request)
                .map(httpServletRequest -> Optional.ofNullable(request.getHeader("X-Forwarded-For"))
                        .orElse(Optional.ofNullable(request.getHeader("Proxy-Client-IP"))
                                .orElse(Optional.ofNullable(request.getHeader("WL-Proxy-Client-IP"))
                                        .orElse(Optional.ofNullable(request.getHeader("HTTP_CLIENT_IP"))
                                                .orElse(Optional.ofNullable(request.getHeader("HTTP_X_FORWARDED_FOR"))
                                                        .orElse(request.getRemoteAddr())))))).orElse(null);
    }

    // SecurityContextHolder에서 회원 id 추출
//    private void setUser() {
//        this.memberId = SecurityUtil.getLoginMemberId();
//    }

    // Api 정보 추출
    private void setApiInfo(JoinPoint joinPoint, Class clazz) {
        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final Method method = methodSignature.getMethod();
        // TODO: @RequestMapping 어노테이션 안붙은 경우에 requestMapping에 null이 들어감 개선 필요
        final RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
        String baseUrl = requestMapping.value()[0];
        Stream.of(GetMapping.class, PutMapping.class, PostMapping.class, DeleteMapping.class, RequestMapping.class)
                .filter(method::isAnnotationPresent)
                .findFirst()
                .ifPresent(mappingClass -> {
                    final Annotation annotation = method.getAnnotation(mappingClass); // TODO: 이거뭐임??
                    try {
                        final String[] methodUrl = (String[]) mappingClass.getMethod("value").invoke(annotation);
                        this.method = (mappingClass.getSimpleName().replace("Mapping", "")).toUpperCase();
                        this.url = String.format("%s%s", baseUrl, methodUrl.length > 0 ? methodUrl[0] : "");
                        this.name = (String) mappingClass.getMethod("name").invoke(annotation);
                    } catch (Exception e) {
                        System.out.println("method, url, name 빼낼때 에러");
                        e.printStackTrace();
                    }
                });
    }

    private void setInputStream(JoinPoint joinPoint, ObjectMapper objectMapper) {
        try {
            final CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
            final String[] parameterNames = codeSignature.getParameterNames();
            final Object[] args = joinPoint.getArgs();
            for (int i = 0; i < parameterNames.length; i++) {
                if (parameterNames[i].equals("request")) {
                    this.body = objectMapper.convertValue(args[i], new TypeReference<Map<String, String>>(){});
                } else {
                    this.parameters.put(parameterNames[i], objectMapper.writeValueAsString(args[i]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
