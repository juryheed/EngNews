package org.mjulikelion.engnews.exception;

public class NotFoundException extends CustomException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
    public NotFoundException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}

