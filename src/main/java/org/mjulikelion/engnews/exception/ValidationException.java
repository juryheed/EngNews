package org.mjulikelion.engnews.exception;

public class ValidationException extends CustomException {
    public ValidationException(ErrorCode errorCode) {
        super(errorCode);
    }
    public ValidationException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
