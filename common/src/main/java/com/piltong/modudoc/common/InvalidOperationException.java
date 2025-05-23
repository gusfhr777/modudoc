package com.piltong.modudoc.common;


// 유효하지 않은 오퍼레이션
// 추후 사용 가능성 있음
public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException(String message) {
        super(message);
    }
}
