package com.piltong.modudoc.server.network;


import com.piltong.modudoc.common.network.*;
import com.piltong.modudoc.server.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.*;
import java.io.*;
import java.util.List;
import java.util.Objects;

// 한 클라이언트와의 소켓 통신을 관리한다.
// 요청 읽기 -> 비즈니스 로직 수행 -> 출력 스트림으로 응답 전송
public class ClientHandler implements Runnable {
    private static final Logger log = LogManager.getLogger(ClientHandler.class);
    private final Socket socket;                    // 연결된 클라이언트 소켓
    private final networkHandlerListener listener;   // 서버 비즈니스 네트워크 처리용 리스너
    protected ObjectInputStream in;                 // 클라이언트 입력 스트림
    protected ObjectOutputStream out;               // 클라이언트 출력 스트림

    // 생성자: 클라이언트 소켓과 서버 로직 핸들러 주입
    public ClientHandler(Socket socket, networkHandlerListener listener) {
        // 변수 할당
        this.listener = listener;
        this.socket = socket;

    }

    @Override
    public void run() {
        log.info("Server-Client Thread Started. {}",socket.getRemoteSocketAddress());
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            log.error("Failed to initialize object streams for socket: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize I/O streams for client connection", e);
        }

        try {
            // 클라이언트 핸들러 무한 반복
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    log.info("Client Thread Message receiveing.");
                    RequestCommandDto<?> dto = (RequestCommandDto<?>) in.readObject(); // 클라이언트 명령 요청 대기
                    log.info("Client Thread Message received.");
                    ClientCommand command = dto.getCommand(); // 커맨드 가져오기
                    // 명령 처리 후 결과 반환
                    Object resultPayload = listener.onCommandReceived(command, dto.getPayload(), this.socket.getRemoteSocketAddress());
                    log.info("resultPayload received: {}", resultPayload);
                    send(command, resultPayload);
                } catch (EOFException e) {
                    // 클라이언트가 연결을 끊은 경우
                    log.warn("클라이언트가 연결을 종료했습니다.");
                    break;
                }

            }

            log.info("Client Thread Interrupted.");
        } catch (IOException | ClassNotFoundException e) {
            // 네트워크 또는 역직렬화 오류 발생 처리
            log.error("Client Thread Failed While Running.");
            listener.onNetworkError(e);
            throw new RuntimeException(e);
        } finally {
            // 자원 정리
            shutdown();
        }
    }


    public void send(ClientCommand command, Object resultPayload) {
        log.info("send() : {}, {}",  command, resultPayload);
        boolean success = true;
        String errorMsg = null;

        if (resultPayload == null) {
            String errMsg = "resultPayload is null";
            throw new IllegalArgumentException(errMsg);
        }
        Serializable payloadDto = null;
        try {

            // payload가 Serializable을 구현한 객체인지 확인한다.
            if (resultPayload instanceof Serializable) {
                payloadDto = (Serializable) resultPayload;
//                log.info("payloadDto1 : {}", payloadDto);
            } else {
                if (resultPayload instanceof Document) {
                    payloadDto = DocMapper.toDto((Document) resultPayload);
//                    log.info("payloadDto2 : {}", payloadDto);
                } else if (resultPayload instanceof Operation) {
                    payloadDto = OperationMapper.toDto((Operation) resultPayload);
//                    log.info("payloadDto3 : {}", payloadDto);
                } else if (resultPayload instanceof User) {
                    payloadDto = UserMapper.toDto((User) resultPayload);
//                    log.info("payloadDto4 : {}", payloadDto);
                } else if (resultPayload instanceof LoginRequest) {
//                    log.info("payloadDto5 : {}", payloadDto);
                    payloadDto = LoginRequestMapper.toDto((LoginRequest) resultPayload);
                } else {
                    String errMsg = "Unkown Model.";
                    log.error(errMsg);
                    throw new RuntimeException(errMsg);
                }
            }
//            log.info("payloadDto : {}", payloadDto);

        } catch (CommandException e) {
            // 처리 중 오류 발생 시 에러 메세지 설정
            log.error("networkListenerImpl Failed: command execution fail.");
            success = false;
            payloadDto = null;
//                        throw new RuntimeException(e);
        }

        // 클라이언트에게 응답 전송
        ResponseCommandDto<Object> response = new ResponseCommandDto<>(
                command,
                payloadDto,
                success,
                errorMsg
        );
        log.info("response : {} ", response);

        try {
            log.info("command send : {}, {}", command, success, payloadDto);
            out.writeObject(response);
            out.flush();

        } catch (IOException e) {
            log.error("Failed to send response for socket: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    // 핸들러 종료
    public void shutdown() {
        log.info("client NetworkHandler shutdown: {}", socket.getRemoteSocketAddress());

        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            log.info("Connection closed: " + Objects.requireNonNull(socket).getRemoteSocketAddress());
        } catch (IOException e) {
            log.error("Client NetworkHandler shutdown failed : socket close error.");
            throw new RuntimeException(e);
        }

    }

}
