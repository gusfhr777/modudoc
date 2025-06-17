package com.piltong.modudoc.server.network;


import com.piltong.modudoc.common.document.Document;
import com.piltong.modudoc.common.document.DocumentDto;
import com.piltong.modudoc.common.network.RequestCommandDto;
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
    private ObjectInputStream in;
    private ObjectOutputStream out;


    public ClientHandler(Socket socket) {
        // 변수 할당
        this.socket = socket;

    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            out = new ObjectOutputStream(os);
            in = new ObjectInputStream(is);

            while (!Thread.currentThread().isInterrupted()) {
                Object msg = in.readObject();

                if (msg instanceof RequestCommandDto<?>) {

                }
            }
        } catch (IOException | ClassNotFoundException e) {
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
