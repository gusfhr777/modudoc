package com.piltong.modudoc.common.network;

import java.io.Serializable;

/**
 * 서버 → 클라이언트로 전송할 응답 명령을 감싸는 DTO입니다.
 *
 * @param <T> 응답 페이로드 타입
 */
public class ResponseCommandDto<T> implements Serializable {

    /** 처리된 명령어 */
    private ClientCommand command;

    /** 성공 시 담기는 결과 데이터 */
    private T payload;

    /** 실패 시 담기는 에러 메시지 */
    private String errorMsg;

    /** 성공 여부 (true: 성공, false: 실패) */
    private boolean isSuccess;


    /**
     * 성공 응답 생성자
     */
    public ResponseCommandDto(ClientCommand command, T payload) {
        this.command = command;
        this.payload = payload;
        this.isSuccess = true;
    }

    /**
     * 실패 응답 생성자
     */
    public ResponseCommandDto(ClientCommand command, String errorMsg) {
        this.command = command;
        this.errorMsg = errorMsg;
        this.isSuccess = false;
    }

    // Getter 및 Setter
    public ClientCommand getCommand() {
        return command;
    }

    public void setCommand(ClientCommand command) {
        this.command = command;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    @Override
    public String toString() {
        return "ResponseCommandDto{" +
                "command=" + command +
                ", payload=" + payload +
                ", errorMsg='" + errorMsg + '\'' +
                ", isSuccess=" + isSuccess +
                '}';
    }
}
