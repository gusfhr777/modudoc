package com.piltong.modudoc.server.network;


import com.piltong.modudoc.common.document.Document;
import com.piltong.modudoc.common.document.DocumentDto;
import com.piltong.modudoc.common.document.DocumentSummary;
import com.piltong.modudoc.common.network.*;
import com.piltong.modudoc.common.operation.Operation;
import com.piltong.modudoc.common.operation.OperationDto;
import com.sun.net.httpserver.Request;

import java.net.*;
import java.io.*;
import java.util.Objects;

// 한 클라이언트와의 소켓 통신을 관리한다.
// 요청 읽기 -> 비즈니스 로직 수행 -> 출력 스트림으로 응답 전송
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final ServerNetworkListener listener;
    protected ObjectInputStream in;
    protected ObjectOutputStream out;


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
                RequestCommandDto<?> dto = (RequestCommandDto<?>) in.readObject(); // 클라이언트 명령 요청 대기
                ClientCommand command = dto.getCommand(); // 커맨드 가져오기

                Object resultPayload;
                boolean success = true;
                String errorMsg = null;

                try {
                    resultPayload = listener.onCommandReceived(command, dto.getPayload());
                } catch (CommandException e) {
                    success = false;
                    resultPayload = null;
                    errorMsg = e.getMessage();
                    System.out.println(errorMsg);
                }

                ResponseCommandDto<Object> response = new ResponseCommandDto<>(
                        command,
                        resultPayload,
                        success,
                        errorMsg
                );

                out.writeObject(response);
                out.flush();



            }
        } catch (IOException | ClassNotFoundException e) {
            listener.onNetworkError(e);
            throw new RuntimeException(e);
        }
    }


    // 핸들러 종료
    public void shutdown() {

        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("Connection closed: " + Objects.requireNonNull(socket).getRemoteSocketAddress());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
