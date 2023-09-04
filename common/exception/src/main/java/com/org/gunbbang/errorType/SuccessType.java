package com.org.gunbbang.errorType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessType {
  /** 200 OK */
  LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다."),
  LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃에 성공했습니다."),
  GET_POST_LIST_SUCCESS(HttpStatus.OK, "게시물 리스트 조회에 성공했습니다."),
  GET_POST_SUCCESS(HttpStatus.OK, "게시물 조회에 성공했습니다."),
  GET_EMOTION_CALENDAR_SUCCESS(HttpStatus.OK, "감정 캘린더 조회에 성공했습니다."),
  GET_EMOTION_SUCCESS(HttpStatus.OK, "감정 조회에 성공했습니다."),
  GET_MYPAGE_SUCCESS(HttpStatus.OK, "마이페이지 조회에 성공했습니다."),
  GET_BAKERY_LIST_SUCCESS(HttpStatus.OK, "건빵집 리스트 조회 성공"),
  GET_BAKERY_REVIEW_LIST_SUCCESS(HttpStatus.OK, "건빵집 리뷰 리스트 조회 성공"),
  GET_MEMBER_REVIEW_BAKERY_LIST_SUCCESS(HttpStatus.OK, "회원이 리뷰한 빵집 리스트 조회 성공"),
  GET_REVIEW_DETAIL_MEMBER_SUCCESS(HttpStatus.OK, "회원이 작성한 리뷰 조회 성공"),
  GET_BEST_BAKERIES_SUCCESS(HttpStatus.OK, "베스트 건빵집 리스트 조회 성공"),
  GET_BAKERY_DETAIL_SUCCESS(HttpStatus.OK, "건빵집 상세 페이지 조회 성공"),
  CANCEL_BOOKMARK_SUCCESS(HttpStatus.OK, "북마크 취소 성공"),
  DELETE_IMAGE_SUCCESS(HttpStatus.OK, "이미지 삭제에 성공했습니다."),
  UPDATE_MEMBER_TYPES_SUCCESS(HttpStatus.OK, "회원 필터칩 변경 성공"),
  GET_MEMBER_TYPES_SUCCESS(HttpStatus.OK, "회원 필터칩 조회 성공"),
  GET_BEST_REVIEWS_SUCCESS(HttpStatus.OK, "베스트 리뷰 조회 성공"),
  SEARCH_BAKERIES_SUCCESS(HttpStatus.OK, "건빵집 검색 성공"),
  GET_BOOKMARKED_BAKERIES_SUCCESS(HttpStatus.OK, "북마크된 건빵집 조회 성공"),
  GET_MEMBER_NICKNAME_SUCCESS(HttpStatus.OK, "현재 접속회원의 닉네임 조회 성공"),
  DELETE_MEMBER_SUCCESS(HttpStatus.OK, "회원 탈퇴에 성공했습니다"),
  UPDATE_MEMBER_NICKNAME_SUCCESS(HttpStatus.OK, "현재 접속회원의 닉네임 변경 성공"),

  /** 201 CREATED */
  SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입이 완료됐습니다."),
  CREATE_REVIEW_SUCCESS(HttpStatus.CREATED, "리뷰 등록이 완료됐습니다."),
  CREATE_BOOKMARK_SUCCESS(HttpStatus.OK, "북마크 추가 성공"),
  CREATE_REVIEW_REPORT_SUCCESS(HttpStatus.CREATED, "리뷰 신고가 완료되었습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String message;

  public int getHttpStatusCode() {
    return httpStatus.value();
  }
}
