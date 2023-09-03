package com.org.gunbbang.support.slack;

import static com.slack.api.model.block.composition.BlockCompositions.plainText;

import com.org.gunbbang.service.VO.ReviewReportSlackVO;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SlackSender {

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

  private static final String REPORT_REVIEW_MESSAGE_TITLE = "리뷰 신고가 접수되었습니다";
  private static final String ERROR_MESSAGE_TITLE = "서버 측 오류로 예상되는 예외 상황이 발생하였습니다";

  private final StringBuilder sb = new StringBuilder();

  public void sendReviewReportMessage(ReviewReportSlackVO vo) throws IOException {
    List layoutBlocks = generateReportLayoutBlock(vo);

    if (Boolean.parseBoolean(isActive)) {
      Slack.getInstance()
          .send(reportWebhookUrl, WebhookPayloads.payload(p -> p.blocks(layoutBlocks)));
    }
  }

  public void sendAlert(Exception error, HttpServletRequest request) throws IOException {
    List layoutBlocks = generateLayoutBlock(error, request);
    if (Boolean.parseBoolean(isActive)) {
      Slack.getInstance()
          .send(errorWebhookUrl, WebhookPayloads.payload(p -> p.blocks(layoutBlocks)));
    }
  }

  // 리뷰 신고 메시지 생성
  private List generateReportLayoutBlock(ReviewReportSlackVO vo) {
    return Blocks.asBlocks(
        getHeader(REPORT_REVIEW_MESSAGE_TITLE),
        Blocks.divider(),
        getSection(generateReportReviewMessage(vo)));
  }

  // 예외 정보 메시지 생성
  private List generateLayoutBlock(Exception error, HttpServletRequest request) {
    return Blocks.asBlocks(
        getHeader(ERROR_MESSAGE_TITLE),
        Blocks.divider(),
        getSection(generateErrorMessage(error)),
        Blocks.divider(),
        getSection(generateErrorPointMessage(request)),
        Blocks.divider());
  }

  private String generateErrorMessage(Exception error) {
    sb.setLength(0);
    sb.append("*[Exception Class Name]*" + NEW_LINE)
        .append(error.getClass().getName())
        .append(DOUBLE_NEW_LINE);
    sb.append("*[Stack Trace]*" + NEW_LINE)
        .append(readRootStackTrace(error))
        .append(DOUBLE_NEW_LINE);
    return sb.toString();
  }

  private String generateReportReviewMessage(ReviewReportSlackVO vo) {
    sb.setLength(0);
    sb.append("*[빵집 이름]*" + NEW_LINE).append(vo.getBakeryName()).append(DOUBLE_NEW_LINE);
    sb.append("*[신고자 id]*" + NEW_LINE).append(vo.getReporterId()).append(DOUBLE_NEW_LINE);
    sb.append("*[신고 id]*" + NEW_LINE).append(vo.getReviewReportId()).append(DOUBLE_NEW_LINE);
    sb.append("*[신고 유형]*" + NEW_LINE).append(vo.getReportCategory()).append(DOUBLE_NEW_LINE);
    sb.append("*[신고 내용]*" + NEW_LINE).append(vo.getReportContent()).append(DOUBLE_NEW_LINE);
    sb.append("*[신고된 리뷰 내용]*" + NEW_LINE).append(vo.getReviewContent()).append(DOUBLE_NEW_LINE);
    sb.append("*[신고 일시]*" + NEW_LINE).append(vo.getReportedAt()).append(DOUBLE_NEW_LINE);

    return sb.toString();
  }

  private String readRootStackTrace(Exception error) {
    return error.getStackTrace()[0].toString()
        + NEW_LINE
        + error.getStackTrace()[1].toString()
        + NEW_LINE
        + error.getStackTrace()[2].toString()
        + NEW_LINE
        + error.getStackTrace()[3].toString()
        + NEW_LINE;
  }

  private LayoutBlock getHeader(String text) {
    return Blocks.header(h -> h.text(plainText(pt -> pt.text(text))));
  }

  private LayoutBlock getSection(String message) {
    return Blocks.section(s -> s.text(BlockCompositions.markdownText(message)));
  }

  private String generateErrorPointMessage(HttpServletRequest request) {
    sb.setLength(0);
    sb.append("*[세부정보]*" + NEW_LINE);
    sb.append("Request URL : ").append(request.getRequestURL().toString()).append(NEW_LINE);
    sb.append("Request Method : ").append(request.getMethod()).append(NEW_LINE);
    sb.append("Request Time : ").append(new Date()).append(NEW_LINE);
    sb.append("env: ").append(env);

    return sb.toString();
  }
}
