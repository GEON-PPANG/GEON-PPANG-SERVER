package com.org.gunbbang.errorType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessType {
    /**
     * 200 OK
     */
    LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다."),
    GET_POST_LIST_SUCCESS(HttpStatus.OK, "게시물 리스트 조회에 성공했습니다."),
    GET_POST_SUCCESS(HttpStatus.OK, "게시물 조회에 성공했습니다."),
    GET_EMOTION_CALENDAR_SUCCESS(HttpStatus.OK, "감정 캘린더 조회에 성공했습니다."),
    GET_EMOTION_SUCCESS(HttpStatus.OK, "감정 조회에 성공했습니다."),
    GET_MYPAGE_SUCCESS(HttpStatus.OK, "마이페이지 조회에 성공했습니다."),
    GET_BAKERY_LIST_SUCCESS(HttpStatus.OK, "건빵집 리스트 조회 성공"),
    GET_BAKERY_REVIEW_LIST_SUCCESS(HttpStatus.OK, "건빵집 리뷰 리스트 조회 성공"),
    GET_BEST_BAKERIES_SUCCESS(HttpStatus.OK, "베스트 건빵집 리스트 조회 성공"),
    GET_BAKERY_DETAIL_SUCCESS(HttpStatus.OK, "건빵집 상세 페이지 조회 성공"),
    CANCEL_BOOKMARK_SUCCESS(HttpStatus.OK, "북마크 취소 성공"),
    DELETE_IMAGE_SUCCESS(HttpStatus.OK, "이미지 삭제에 성공했습니다."),
    UPDATE_MEMBER_TYPES_SUCCESS(HttpStatus.OK, "회원 필터칩 변경 성공"),

    /**
     * 201 CREATED
     */
    SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입이 완료됐습니다."),
    CREATE_REVIEW_SUCCESS(HttpStatus.CREATED, "리뷰 등록이 완료됐습니다."),
    CREATE_EMOTION_SUCCESS(HttpStatus.CREATED, "감정 기록에 성공했습니다."),
    CREATE_VOTE_SUCCESS(HttpStatus.CREATED, "투표가 완료됐습니다."),
    CREATE_BOOKMARK_SUCCESS(HttpStatus.OK, "북마크 추가 성공"),

    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}

