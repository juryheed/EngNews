package org.mjulikelion.engnews.exception;

public class ConflictException extends CustomException {
    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
    public ConflictException(ErrorCode errorCode, String detail){
        super(errorCode,detail);
    }
}
