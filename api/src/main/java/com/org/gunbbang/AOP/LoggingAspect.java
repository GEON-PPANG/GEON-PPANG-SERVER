package com.org.gunbbang.AOP;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.gunbbang.AOP.ApiInfo.RequestApiInfo;
import com.org.gunbbang.AOP.ApiInfo.SearchApiInfo;
import com.org.gunbbang.AOP.ApiInfo.SignupApiInfo;
import com.org.gunbbang.AOP.annotation.BakeryIdApiLog;
import com.org.gunbbang.AOP.annotation.ReviewIdApiLog;
import com.org.gunbbang.AOP.annotation.SearchApiLog;
import com.org.gunbbang.AOP.annotation.SignupApiLog;
import com.org.gunbbang.AOP.logInfo.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

  private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
  private final ObjectMapper objectMapper;

  // AOP를 적용할 패키지 범위 설정
  @Pointcut("within(com.org.gunbbang.controller..*)")
  public void onRequest() {}

  @Pointcut(
      "onRequest() "
          + "&& !@annotation(com.org.gunbbang.AOP.annotation.SearchApiLog) "
          + "&& !@annotation(com.org.gunbbang.AOP.annotation.SignupApiLog) "
          + "&& !@annotation(com.org.gunbbang.AOP.annotation.ReviewIdApiLog) "
          + "&& !@annotation(com.org.gunbbang.AOP.annotation.BakeryIdApiLog) ")
  private void defaultRequest() {}

  @Around("@annotation(reviewIdApiLog) && args(reviewId,..)")
  public Object doReviewIdLogging(
      ProceedingJoinPoint joinPoint, ReviewIdApiLog reviewIdApiLog, Long reviewId)
      throws Throwable {
    final RequestApiInfo apiInfo =
        new RequestApiInfo(joinPoint, joinPoint.getTarget().getClass(), objectMapper);

    final ReviewIdLogInfo reviewIdLogInfo =
        new ReviewIdLogInfo(
            apiInfo.getUrl(),
            apiInfo.getName(),
            apiInfo.getMethod(),
            apiInfo.getHeader().toString(),
            objectMapper.writeValueAsString(apiInfo.getParameters()),
            objectMapper.writeValueAsString(apiInfo.getBody()),
            apiInfo.getIpAddress(),
            reviewId
            //                apiInfo.getMemberId()
            );

    System.out.println("리뷰 id 로깅");
    return doLogging(joinPoint, reviewIdLogInfo);
  }

  @Around("@annotation(bakeryIdLog) && args(bakeryId,..)")
  public Object doBakeryIdLogging(
      ProceedingJoinPoint joinPoint, BakeryIdApiLog bakeryIdLog, Long bakeryId) throws Throwable {
    final RequestApiInfo apiInfo =
        new RequestApiInfo(joinPoint, joinPoint.getTarget().getClass(), objectMapper);

    final BakeryIdLogInfo logInfo =
        new BakeryIdLogInfo(
            apiInfo.getUrl(),
            apiInfo.getName(),
            apiInfo.getMethod(),
            apiInfo.getHeader().toString(),
            apiInfo.getParameters(),
            apiInfo.getBody(),
            apiInfo.getIpAddress(),
            bakeryId
            //                apiInfo.getMemberId()
            );

    System.out.println("베이커리 id 로깅");
    return doLogging(joinPoint, logInfo);
  }

  @Around("defaultRequest()")
  public Object doDefaultLogging(ProceedingJoinPoint joinPoint) throws Throwable {
    final RequestApiInfo apiInfo =
        new RequestApiInfo(joinPoint, joinPoint.getTarget().getClass(), objectMapper);

    final LogInfo logInfo =
        new LogInfo(
            apiInfo.getUrl(),
            apiInfo.getName(),
            apiInfo.getMethod(),
            apiInfo.getHeader().toString(),
            apiInfo.getParameters(),
            apiInfo.getBody(),
            apiInfo.getIpAddress()
            //                apiInfo.getMemberId()
            );

    System.out.println("기본 로깅");
    return doLogging(joinPoint, logInfo);
  }

  @Around("@annotation(signupApiLog)")
  public Object doSignupLogging(ProceedingJoinPoint joinPoint, SignupApiLog signupApiLog)
      throws Throwable {

    final SignupApiInfo apiInfo =
        new SignupApiInfo(joinPoint, joinPoint.getTarget().getClass(), objectMapper);

    final SignupLogInfo signupLogInfo =
        new SignupLogInfo(
            apiInfo.getUrl(),
            apiInfo.getName(),
            apiInfo.getMethod(),
            apiInfo.getHeader().toString(),
            apiInfo.getParameters(),
            apiInfo.getBody(),
            apiInfo.getIpAddress(),
            apiInfo.getIsGlutenFree(),
            apiInfo.getIsVegan(),
            apiInfo.getIsNutFree(),
            apiInfo.getIsSugarFree(),
            apiInfo.getIsNutrientOpen(),
            apiInfo.getIsIngredientOpen(),
            apiInfo.getIsNotOpen(),
            apiInfo.getMainPurpose()
            //                apiInfo.getMemberId()

            );

    System.out.println("회원가입 로깅");
    return doLogging(joinPoint, signupLogInfo);
  }

  @Around("@annotation(searchApiLog)")
  public Object doSearchLogging(ProceedingJoinPoint joinPoint, SearchApiLog searchApiLog)
      throws Throwable {

    final SearchApiInfo apiInfo =
        new SearchApiInfo(joinPoint, joinPoint.getTarget().getClass(), objectMapper);

    final SearchLogInfo searchLogInfo =
        new SearchLogInfo(
            apiInfo.getUrl(),
            apiInfo.getName(),
            apiInfo.getMethod(),
            apiInfo.getHeader().toString(),
            apiInfo.getParameters(),
            apiInfo.getBody(),
            apiInfo.getIpAddress(),
            apiInfo.getSearchKeyword()
            //                apiInfo.getMemberId()
            );

    System.out.println("서치 로깅");
    return doLogging(joinPoint, searchLogInfo);
  }

  private Object doLogging(ProceedingJoinPoint joinPoint, LogInfo logInfo) throws Throwable {
    try {

      final Object result = joinPoint.proceed(joinPoint.getArgs());
      final String logMessage = objectMapper.writeValueAsString(Map.entry("logInfo", logInfo));
      logger.info(logMessage);

      return result;

    } catch (Exception e) {
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
      logger.error(logMessage);

      throw e;
    }
  }
}
