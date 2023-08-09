package com.org.gunbbang.AOP.ApiInfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.aspectj.lang.JoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Getter
public class SearchApiInfo extends RequestApiInfo {
  private String searchKeyword;

  public SearchApiInfo(JoinPoint joinPoint, Class clazz, ObjectMapper objectMapper)
      throws UnsupportedEncodingException {
    super(joinPoint, clazz, objectMapper);
    setSearchKeyword();
  }

  private void setSearchKeyword() throws UnsupportedEncodingException {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

    // 쿼리 스트링 가져오기
    String queryString = URLDecoder.decode(request.getQueryString(), "UTF-8");

    // bakeryName 파라미터 값 가져오기
    // TODO: 한글로 가져올 수는 없을지??
    String bakeryName = null;
    if (queryString != null) {
      String[] params = queryString.split("&");
      for (String param : params) {
        String[] keyValue = param.split("=");
        if (keyValue.length == 2 && "bakeryName".equals(keyValue[0])) {
          bakeryName = keyValue[1];
          break;
        }
      }
    }

    this.searchKeyword = bakeryName;
  }
}
