package org.mjulikelion.engnews.exception;

public class ForbiddenException extends CustomException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
    public ForbiddenException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}

