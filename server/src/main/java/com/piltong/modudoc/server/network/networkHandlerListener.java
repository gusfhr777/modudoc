package com.piltong.modudoc.server.network;


import com.piltong.modudoc.common.network.ClientCommand;
import com.piltong.modudoc.common.network.CommandException;

/**
 * 서버 네트워크 핸들러와 서비스 로직 간의 연결을 위한 인터페이스입니다.
 * 네트워크 핸들러는 클라이언트로부터 수신된 Command 요청을 이 인터페이스를 통해 서비스 로직에 전달하고,
 * 처리 결과를 반환합니다.
 */
public interface networkHandlerListener {

    /**
     * 클라이언트로부터 명령 요청을 수신했을 때 호출됩니다.
     *
     * @param <T>       요청 페이로드의 타입
     * @param <R>       응답 페이로드의 타입
     * @param command   수신된 {@link ClientCommand} 명령
     * @param payload   명령 실행에 필요한 데이터
     * @return 처리 결과 데이터. {@code command}에 따라 다음과 같은 타입으로 반환됩니다:
     *         <ul>
     *           <li>{@link ClientCommand#CREATE_DOCUMENT}         → {@link com.piltong.modudoc.server.model.Document}</li>
     *           <li>{@link ClientCommand#READ_DOCUMENT}           → {@link com.piltong.modudoc.server.model.Document}</li>
     *           <li>{@link ClientCommand#READ_DASHBOARD} → {@code List<com.piltong.modudoc.server.model.Document>}</li>
     *           <li>{@link ClientCommand#UPDATE_DOCUMENT}         → {@link com.piltong.modudoc.server.model.Document}</li>
     *           <li>{@link ClientCommand#DELETE_DOCUMENT}         → {@code String} (삭제된 문서 ID)</li>
     *           <li>{@link ClientCommand#PROPAGATE_OPERATION}     → {@link com.piltong.modudoc.server.model.Operation}</li>
     *         </ul>
     * @throws CommandException 비즈니스 로직 처리 중 에러가 발생한 경우 던집니다.
     */
    <T, R> R onCommandReceived(ClientCommand command, T payload) throws CommandException;

    /**
     * 네트워크 연결 또는 I/O 처리 중 예외가 발생했을 때 호출됩니다.
     *
     * @param t 발생한 예외 {@link Throwable} 객체로, 스택 트레이스 등을 통해 원인 분석이 가능합니다.
     */
    void onNetworkError(Throwable t);

}
