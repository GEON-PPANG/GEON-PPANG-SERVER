package com.org.gunbbang.AOP;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final ObjectMapper objectMapper;

    // AOP를 적용할 패키지 범위 설정
    @Pointcut("within(com.org.gunbbang.controller..*)")
    public void onRequest() {}

    // 지정된 패턴에 해당하는 메소드의 실행되기 전, 실행된 후 모두에서 동작
    @Around("onRequest()")
    public Object requestLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        // API의 정보 담는 클래스
        final RequestApiInfo apiInfo = new RequestApiInfo(joinPoint, joinPoint.getTarget().getClass(), objectMapper);

        // TODO: LogInfo 클래스 코드가 정확히 어떻게 될지??
        final LogInfo logInfo = new LogInfo(
                apiInfo.getUrl(),
                apiInfo.getName(),
                apiInfo.getMethod(),
                apiInfo.getHeader().toString(),
                objectMapper.writeValueAsString(apiInfo.getParameters()),
                objectMapper.writeValueAsString(apiInfo.getBody()),
                apiInfo.getIpAddress()
//                apiInfo.getMemberId()
        );

        try {
            // TODO: 이거 이해
            final Object result = joinPoint.proceed(joinPoint.getArgs());

            // GET 메서드가 아닌 로그만 수집
//            if(!logInfo.getMethod().equals("GET")) {
//                final String logMessage = objectMapper.writeValueAsString(Map.entry("logInfo", logInfo));
//                logger.info(logMessage);
//            }

            final String logMessage = objectMapper.writeValueAsString(Map.entry("logInfo", logInfo));
            logger.info(logMessage); // TODO: 여기 두 번 찍힘 왜???

            return result;

        } catch (Exception e) {
            final StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            final String exceptionAsString = sw.toString();

            // 발생 Exception 설정
            logInfo.setException(exceptionAsString);
            final String logMessage = objectMapper.writeValueAsString(logInfo);
            logger.error(logMessage);

            throw e;
        }
    }
}
