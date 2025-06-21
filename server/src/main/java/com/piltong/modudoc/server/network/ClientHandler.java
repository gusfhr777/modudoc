package com.piltong.modudoc.server.network;


import com.piltong.modudoc.common.network.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.*;
import java.io.*;
import java.util.Objects;

// 한 클라이언트와의 소켓 통신을 관리한다.
// 요청 읽기 -> 비즈니스 로직 수행 -> 출력 스트림으로 응답 전송
public class ClientHandler implements Runnable {
    private static final Logger log = LogManager.getLogger(ClientHandler.class);
    private final Socket socket;                    // 연결된 클라이언트 소켓
    private final ServerNetworkListener listener;   // 서버 로직 처리 핸들러
    protected ObjectInputStream in;                 // 클라이언트 입력 스트림
    protected ObjectOutputStream out;               // 클라이언트 출력 스트림

    // 생성자: 클라이언트 소켓과 서버 로직 핸들러 주입
    public ClientHandler(Socket socket, ServerNetworkListener listener) {
        // 변수 할당
        this.listener = listener;
        this.socket = socket;

    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());



            // 클라이언트 핸들러 무한 반복
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    RequestCommandDto<?> dto = (RequestCommandDto<?>) in.readObject(); // 클라이언트 명령 요청 대기
                    ClientCommand command = dto.getCommand(); // 커맨드 가져오기

                    Object resultPayload;
                    boolean success = true;
                    String errorMsg = null;

                    try {
                        // 명령 처리 후 결과 반환
                        resultPayload = listener.onCommandReceived(command, dto.getPayload());
                    } catch (CommandException e) {
                        // 처리 중 오류 발생 시 에러 메세지 설정
                        success = false;
                        resultPayload = null;
                        errorMsg = e.getMessage();
                        System.out.println(errorMsg);
                    }

                    // 클라이언트에게 응답 전송
                    ResponseCommandDto<Object> response = new ResponseCommandDto<>(
                            command,
                            resultPayload,
                            success,
                            errorMsg
                    );

                    out.writeObject(response);
                    out.flush();
                    log.info("Command Sent : " + command + " : " + "errorMsg");

                } catch (EOFException e) {
                    // 클라이언트가 연결을 끊은 경우
                    log.warn("클라이언트가 연결을 종료했습니다.");
                    break;
                }
            }

            log.info("Client Thread Interrupted.");
        } catch (IOException | ClassNotFoundException e) {
            // 네트워크 또는 역직렬화 오류 발생 처리
            listener.onNetworkError(e);
            throw new RuntimeException(e);
        } finally {
            // 자원 정리
            shutdown();
        }
    }


    // 핸들러 종료
    public void shutdown() {

        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            log.info("Connection closed: " + Objects.requireNonNull(socket).getRemoteSocketAddress());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
