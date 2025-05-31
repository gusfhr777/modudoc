package com.piltong.modudoc.common.network;

// 서비스 로직 측에서 커맨드 처리 도중 발생한 오류.
// 네트워크 핸들러 측에서 처리한다.
public class CommandException extends RuntimeException {
    public CommandException(String message) {
        super(message);
    }
}
