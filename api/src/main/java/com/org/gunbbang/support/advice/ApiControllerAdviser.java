package com.org.gunbbang.support.advice;

import com.auth0.jwt.exceptions.*;
import com.org.gunbbang.HandleException;
import com.org.gunbbang.common.DTO.ApiResponse;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.support.slack.SlackSender;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ApiControllerAdviser {

  private final SlackSender slackSender;

  // 요청이 스프링 MVC 유효성 검사에 실패한 경우
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ApiResponse handleMethodArgumentNotValidException(
      final MethodArgumentNotValidException e) {
    FieldError fieldError = Objects.requireNonNull(e.getFieldError());
    return ApiResponse.error(
        ErrorType.REQUEST_VALIDATION_EXCEPTION,
        String.format("%s. (%s)", fieldError.getDefaultMessage(), fieldError.getField()));
  }

  // 필요한 요청 파라미터 값이 오지 않은 경우
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MissingServletRequestParameterException.class)
  protected ApiResponse handleMissingServletRequestParameterException(
      final MissingServletRequestParameterException e) {
    return ApiResponse.error(
        ErrorType.NO_REQUEST_PARAMETER_EXCEPTION,
        String.format(
            "%s. (%s)",
            ErrorType.NO_REQUEST_PARAMETER_EXCEPTION.getMessage(), e.getParameterName()));
  }

  // 요청 필드의 타입이 일치하지 않은 경우
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ApiResponse handleMethodArgumentTypeMismatchException(
      final MethodArgumentTypeMismatchException e) {
    return ApiResponse.error(
        ErrorType.PARAMETER_TYPE_MISMATCH_EXCEPTION,
        String.format(
            "%s. (%s)", ErrorType.PARAMETER_TYPE_MISMATCH_EXCEPTION.getMessage(), e.getName()));
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BindException.class)
  protected ApiResponse handleBindingException(final BindException e) {
    return ApiResponse.error(
        ErrorType.REQUEST_BIND_EXCEPTION,
        String.format("%s. (%s)", ErrorType.REQUEST_BIND_EXCEPTION, e.getBindingResult()));
  }

  @ExceptionHandler(SignatureVerificationException.class)
  public ResponseEntity<ApiResponse> handleSignatureVerificationException(
      final SignatureVerificationException e) {
    e.printStackTrace();
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(ApiResponse.error(ErrorType.NOT_VALID_TOKEN_EXCEPTION));
  }

  @ExceptionHandler(TokenExpiredException.class)
  public ResponseEntity<ApiResponse> handleTokenExpiredException() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(ApiResponse.error(ErrorType.EXPIRED_TOKEN_EXCEPTION));
  }

  @ExceptionHandler(JWTDecodeException.class)
  public ResponseEntity<ApiResponse> handleJWTDecodeException(JWTDecodeException e) {
    e.printStackTrace();
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(ApiResponse.error(ErrorType.JWT_DECODE_FAIL_EXCEPTION));
  }

  @ExceptionHandler(JWTVerificationException.class)
  public ResponseEntity<ApiResponse> handleJWTVerificationException(Exception e) {
    e.printStackTrace();
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(ApiResponse.error(401, e.getMessage()));
  }

  @ExceptionHandler(HandleException.class)
  protected ResponseEntity<ApiResponse> handleCustomException(final HandleException e) {
    log.error("%%%%%%%%%% " + e.getMessage() + " %%%%%%%%%%");
    return ResponseEntity.status(e.getHttpStatus())
        .body(ApiResponse.error(e.getErrorType(), e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ApiResponse> handleException(
      final Exception e, HttpServletRequest request) throws Exception {
    slackSender.sendAlert(e, request);
    log.error("%%%%%%%%%% " + e.getMessage() + "%%%%%%%%%%");
    e.printStackTrace();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.error(ErrorType.INTERNAL_SERVER_ERROR));
  }
}
