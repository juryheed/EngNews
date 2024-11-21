package org.mjulikelion.engnews.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //UnauthorizedException
    INVALID_EMAIL_OR_PASSWORD("4010", "등록되지 않은 이메일 또는 비밀번호를 잘못 입력했습니다."),
    COOKIE_NOT_FOUND("4011", "쿠키를 찾을 수 없습니다."),
    INVALID_TOKEN("4012", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("4013", "만료된 토큰입니다."),
    INVALID_ARTICLE("4014", "뉴스 기사를 불러 올 수 없습니다."),

    //ForbiddenException
    NO_ACCESS("4030", "접근 권한이 없습니다."),

    //NotFoundException
    USER_NOT_FOUND("4040", "유저를 찾을 수 없습니다."),
    WORD_LIKE_NOT_FOUND("4042", "찜한 단어를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND("4043", "카테고리를 찾을 수 없습니다."),
    KEYWORD_NOT_FOUMD("4044", "키워드를 찾을수 없습니다."),
    ARTICLE_LIKE_NOT_FOUND("4045", "찜한 뉴스를 찾을 수 없습니다."),

    //ConflictException
    DUPLICATED_NAME("4090","이미 사용 중인 이름입니다."),
    DUPLICATED_EMAIL("4091", "이미 사용 중인 이메일입니다."),

    //ValidationException
    NOT_NULL("9001", "필수값이 누락되었습니다."),
    NOT_BLANK("9002", "필수값이 빈 값이거나 공백으로 되어있습니다."),
    REGEX("9003", "이메일 형식에 맞지 않습니다."),
    LENGTH("9004", "길이가 유효하지 않습니다.");

    private final String code;
    private final String message;

    //Dto의 어노테이션을 통해 발생한 에러코드를 반환
    public static ErrorCode resolveValidationErrorCode(String code) {
        return switch (code) {
            case "NotNull" -> NOT_NULL;
            case "NotBlank" -> NOT_BLANK;
            case "Pattern" -> REGEX;
            case "Length" -> LENGTH;
            default -> throw new IllegalArgumentException("Unexpected value: " + code);
        };
    }
}

