package com.org.gunbbang.AOP;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.gunbbang.AOP.ApiInfo.RequestApiInfo;
import com.org.gunbbang.AOP.logInfo.*;
import com.org.gunbbang.jwt.service.JwtService;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LoggingAspect {

  private final ObjectMapper objectMapper;
  private final JwtService jwtService;

  // AOP를 적용할 패키지 범위 설정
  @Pointcut("within(com.org.gunbbang.controller..*)")
  public void onRequest() {}

  @Around("onRequest()")
  public Object doDefaultLogging(ProceedingJoinPoint joinPoint) throws Throwable {
    final RequestApiInfo apiInfo =
        new RequestApiInfo(joinPoint, joinPoint.getTarget().getClass(), jwtService);

    final LogInfo logInfo =
        new LogInfo(
            apiInfo.getMemberId(),
            apiInfo.getUrl(),
            apiInfo.getName(),
            apiInfo.getMethod(),
            apiInfo.getParameters(),
            apiInfo.getBody(),
            apiInfo.getLogTimeStamp());

    System.out.println("기본 로깅");
    return doLogging(joinPoint, logInfo);
  }

  private Object doLogging(ProceedingJoinPoint joinPoint, LogInfo logInfo) throws Throwable {
    final Object result = joinPoint.proceed(joinPoint.getArgs());
    try {
      final String logMessage = objectMapper.writeValueAsString(Map.entry("logInfo", logInfo));
      log.info(logMessage);
    } catch (Exception e) {
      log.error("%%%%%%%%%% 로깅 남기는 과정에서 에러 발생 %%%%%%%%%%");
      final StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      final String exceptionAsString = sw.toString();

      String[] lines = exceptionAsString.split(System.lineSeparator());
      StringBuilder truncatedStackTrace = new StringBuilder();
      int limit = Math.min(5, lines.length);
      for (int i = 0; i < limit; i++) {
        truncatedStackTrace.append(lines[i]).append(System.lineSeparator());
      }

      logInfo.setException(truncatedStackTrace.toString());
      logInfo.setExceptionSimpleName(e.getClass().getSimpleName());
      final String logMessage = objectMapper.writeValueAsString(logInfo);
      log.error(logMessage);
    }
    return result;
  }
}
