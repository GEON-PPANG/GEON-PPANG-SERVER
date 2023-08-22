package com.org.gunbbang.support.slack;

import static com.slack.api.model.block.composition.BlockCompositions.plainText;

import com.org.gunbbang.service.vo.ReviewReportSlackVO;
import com.slack.api.Slack;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.BlockCompositions;
import com.slack.api.webhook.WebhookPayloads;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackUtil {

  private static final String DOUBLE_NEW_LINE = "\n\n";

  private static final String NEW_LINE = "\n";

  @Value("${slack.webhook.error-url}")
  private String errorWebhookUrl;

  @Value("${slack.webhook.report-url}")
  private String reportWebhookUrl;

  @Value("${slack.isActive}")
  private String isActive;

  @Value("${spring.profiles.active}")
  private String env;

  private StringBuilder sb = new StringBuilder();

  public void sendReviewReportMessage(ReviewReportSlackVO vo) throws IOException {
    List layoutBlocks = generateReportLayoutBlock(vo);
    System.out.println("isActive: " + isActive);

    if (Boolean.parseBoolean(isActive)) {
      Slack.getInstance()
          .send(reportWebhookUrl, WebhookPayloads.payload(p -> p.blocks(layoutBlocks)));
    }
  }

  public void sendAlert(Exception error, HttpServletRequest request) throws IOException {

    List layoutBlocks = generateLayoutBlock(error, request);
    System.out.println("isActive: " + isActive);

    if (Boolean.parseBoolean(isActive)) {
      Slack.getInstance()
          .send(errorWebhookUrl, WebhookPayloads.payload(p -> p.blocks(layoutBlocks)));
    }
  }

  // 리뷰 신고 메시지 생성
  private List generateReportLayoutBlock(ReviewReportSlackVO vo) {
    return Blocks.asBlocks(
        getHeader(SlackMessage.REPORT_REVIEW_TITLE),
        Blocks.divider(),
        getSection(generateReportReviewMessage(vo)));
  }

  // 예외 정보 메시지 생성
  private List generateLayoutBlock(Exception error, HttpServletRequest request) {
    return Blocks.asBlocks(
        getHeader("서버 측 오류로 예상되는 예외 상황이 발생하였습니다."),
        Blocks.divider(),
        getSection(generateErrorMessage(error)),
        Blocks.divider(),
        getSection(generateErrorPointMessage(request)),
        Blocks.divider());
  }

  private String generateErrorMessage(Exception error) {
    sb.setLength(0);
    sb.append("*[Exception]*" + NEW_LINE + error.toString() + DOUBLE_NEW_LINE);
    sb.append("*[From]*" + NEW_LINE + readRootStackTrace(error) + DOUBLE_NEW_LINE);

    return sb.toString();
  }

  private String generateReportReviewMessage(ReviewReportSlackVO vo) {
    sb.setLength(0);
    sb.append("*[빵집 이름]*" + NEW_LINE + vo.getBakeryName() + DOUBLE_NEW_LINE);
    sb.append("*[신고자 id]*" + NEW_LINE + vo.getReporterId() + DOUBLE_NEW_LINE);
    sb.append("*[신고 id]*" + NEW_LINE + vo.getReviewReportId() + DOUBLE_NEW_LINE);
    sb.append("*[신고 유형]*" + NEW_LINE + vo.getReportCategory() + DOUBLE_NEW_LINE);
    sb.append("*[신고 내용]*" + NEW_LINE + vo.getReportContent() + DOUBLE_NEW_LINE);
    sb.append("*[신고된 리뷰 내용]*" + NEW_LINE + vo.getReviewContent() + DOUBLE_NEW_LINE);
    sb.append("*[신고 일시]*" + NEW_LINE + vo.getReportedAt() + DOUBLE_NEW_LINE);

    return sb.toString();
  }

  private String readRootStackTrace(Exception error) {
    return error.getStackTrace()[0].toString();
  }

  private LayoutBlock getHeader(String text) {
    return Blocks.header(h -> h.text(plainText(pt -> pt.text(text))));
  }

  // 에러 로그 메시지 내용 return
  private LayoutBlock getSection(String message) {
    return Blocks.section(s -> s.text(BlockCompositions.markdownText(message)));
  }

  // HttpServletRequest를 사용하여 예외발생 요청에 대한 정보 메시지 생성
  private String generateErrorPointMessage(HttpServletRequest request) {
    sb.setLength(0);
    sb.append("*[세부정보]*" + NEW_LINE);
    sb.append("Request URL : " + request.getRequestURL().toString() + NEW_LINE);
    sb.append("Request Method : " + request.getMethod() + NEW_LINE);
    sb.append("Request Time : " + new Date() + NEW_LINE);
    sb.append("env: " + env);

    return sb.toString();
  }
}
