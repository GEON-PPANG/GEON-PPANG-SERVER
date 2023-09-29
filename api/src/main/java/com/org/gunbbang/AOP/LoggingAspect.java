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

  //  @Pointcut(
  //      "onRequest() "
  //          + "&& !@annotation(com.org.gunbbang.AOP.annotation.SearchApiLog) "
  //          + "&& !@annotation(com.org.gunbbang.AOP.annotation.SignupApiLog) "
  //          + "&& !@annotation(com.org.gunbbang.AOP.annotation.ReviewIdApiLog) "
  //          + "&& !@annotation(com.org.gunbbang.AOP.annotation.BakeryIdApiLog) ")
  //  private void defaultRequest() {}

  //  @Around("@annotation(reviewIdApiLog) && args(reviewId,..)")
  //  public Object doReviewIdLogging(
  //      ProceedingJoinPoint joinPoint, ReviewIdApiLog reviewIdApiLog, Long reviewId)
  //      throws Throwable {
  //    final RequestApiInfo apiInfo =
  //        new RequestApiInfo(joinPoint, joinPoint.getTarget().getClass(), objectMapper,
  // jwtService);
  //
  //    final ReviewIdLogInfo reviewIdLogInfo =
  //        new ReviewIdLogInfo(
  //            apiInfo.getMemberId(),
  //            apiInfo.getUrl(),
  //            apiInfo.getName(),
  //            apiInfo.getMethod(),
  //            apiInfo.getParameters(),
  //            apiInfo.getBody(),
  //            reviewId
  //            //                apiInfo.getMemberId()
  //            );
  //
  //    System.out.println("리뷰 id 로깅");
  //    return doLogging(joinPoint, reviewIdLogInfo);
  //  }

  //  @Around("@annotation(bakeryIdLog) && args(bakeryId,..)")
  //  public Object doBakeryIdLogging(
  //      ProceedingJoinPoint joinPoint, BakeryIdApiLog bakeryIdLog, Long bakeryId) throws Throwable
  // {
  //    final RequestApiInfo apiInfo =
  //        new RequestApiInfo(joinPoint, joinPoint.getTarget().getClass(), objectMapper,
  // jwtService);
  //
  //    final BakeryIdLogInfo logInfo =
  //        new BakeryIdLogInfo(
  //            apiInfo.getMemberId(),
  //            apiInfo.getUrl(),
  //            apiInfo.getName(),
  //            apiInfo.getMethod(),
  //            apiInfo.getParameters(),
  //            apiInfo.getBody(),
  //            bakeryId
  //            //                apiInfo.getMemberId()
  //            );
  //
  //    System.out.println("베이커리 id 로깅");
  //    return doLogging(joinPoint, logInfo);
  //  }

  @Around("onRequest()")
  public Object doDefaultLogging(ProceedingJoinPoint joinPoint) throws Throwable {
    final RequestApiInfo apiInfo =
        new RequestApiInfo(joinPoint, joinPoint.getTarget().getClass(), objectMapper, jwtService);

    final LogInfo logInfo =
        new LogInfo(
            apiInfo.getMemberId(),
            apiInfo.getUrl(),
            apiInfo.getName(),
            apiInfo.getMethod(),
            apiInfo.getParameters(),
            apiInfo.getBody()
            //                apiInfo.getMemberId()
            );

    System.out.println("기본 로깅");
    return doLogging(joinPoint, logInfo);
  }

  //  @Around("@annotation(signupApiLog)")
  //  public Object doSignupLogging(ProceedingJoinPoint joinPoint, SignupApiLog signupApiLog)
  //      throws Throwable {
  //
  //    final SignupApiInfo apiInfo =
  //        new SignupApiInfo(joinPoint, joinPoint.getTarget().getClass(), objectMapper);
  //
  //    final SignupLogInfo signupLogInfo =
  //        new SignupLogInfo(
  //            apiInfo.getMemberId(),
  //            apiInfo.getUrl(),
  //            apiInfo.getName(),
  //            apiInfo.getMethod(),
  //            apiInfo.getParameters(),
  //            apiInfo.getBody());
  //
  //    System.out.println("회원가입 로깅");
  //    return doLogging(joinPoint, signupLogInfo);
  //  }

  //  @Around("@annotation(searchApiLog)")
  //  public Object doSearchLogging(ProceedingJoinPoint joinPoint, SearchApiLog searchApiLog)
  //      throws Throwable {
  //
  //    final SearchApiInfo apiInfo =
  //        new SearchApiInfo(joinPoint, joinPoint.getTarget().getClass(), objectMapper,
  // jwtService);
  //
  //    final SearchLogInfo searchLogInfo =
  //        new SearchLogInfo(
  //            apiInfo.getMemberId(),
  //            apiInfo.getUrl(),
  //            apiInfo.getName(),
  //            apiInfo.getMethod(),
  //            apiInfo.getParameters(),
  //            apiInfo.getBody(),
  //            apiInfo.getSearchKeyword()
  //            //                apiInfo.getMemberId()
  //            );
  //
  //    System.out.println("서치 로깅");
  //    return doLogging(joinPoint, searchLogInfo);
  //  }

  private Object doLogging(ProceedingJoinPoint joinPoint, LogInfo logInfo) throws Throwable {
    try {

      final Object result = joinPoint.proceed(joinPoint.getArgs());
      final String logMessage = objectMapper.writeValueAsString(Map.entry("logInfo", logInfo));
      log.info(logMessage);

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
      log.error(logMessage);

      throw e;
    }
  }
}
