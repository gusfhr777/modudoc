package com.piltong.modudoc.common.network;

import java.io.Serializable;

/**
 * 클라이언트 → 서버로 전송할 요청 명령을 감싸는 DTO입니다.
 *
 * @param <T> 요청 페이로드 타입
 */
public class RequestCommandDto<T> implements Serializable {

    /** 실행할 명령어 */
    private ClientCommand command;

    /** 명령 실행에 필요한 데이터 (payload) */
    private T payload;


    // 요청 명령어 생성자
    public RequestCommandDto(ClientCommand command, T payload) {
        this.command = command;
        this.payload = payload;
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

    @Override
    public String toString() {
        return "RequestCommandDto{" +
                "command=" + command +
                ", payload=" + payload +
                '}';
    }
}
