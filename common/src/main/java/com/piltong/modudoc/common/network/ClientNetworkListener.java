package com.piltong.modudoc.common.network;

import com.piltong.modudoc.common.operation.Operation;

/**
 * 클라이언트 네트워크 핸들러와 서비스 로직 간의 연결을 위한 인터페이스입니다.
 * 서버로부터 수신된 명령 성공/실패, Operation 브로드캐스트, 네트워크 오류를 처리합니다.
 */
public interface ClientNetworkListener {

    /**
     * 클라이언트가 보낸 명령(request)에 대한 성공 응답을 수신했을 때 호출됩니다.
     *
     * @param <T>       페이로드의 타입
     * @param command   처리된 {@link ClientCommand}
     * @param payload   성공 시 반환된 데이터. 명령의 응답 데이터가 담긴다. ClientCommand 참조.
     */
    <T> void onCommandSuccess(ClientCommand command, T payload);

    /**
     * 클라이언트가 보낸 명령(request)이 비즈니스 로직상 실패했을 때 호출됩니다.
     *
     * @param command       실패를 일으킨 {@link ClientCommand}
     * @param errorMessage  실패 원인을 설명하는 메시지
     */
    void onCommandFailure(ClientCommand command, String errorMessage);

    /**
     * 서버 또는 다른 클라이언트로부터 브로드캐스트된 Operation을 수신했을 때 호출됩니다.
     *
     * @param op 수신된 {@link Operation} 객체
     */
    void onOperationReceived(Operation op);


    /**
     * 서버 또는 네트워크 처리 중 예외가 발생했을 때 호출됩니다.
     *
     * @param t 발생한 예외 {@link Throwable} 객체
     */
    void onNetworkError(Throwable t);

}
