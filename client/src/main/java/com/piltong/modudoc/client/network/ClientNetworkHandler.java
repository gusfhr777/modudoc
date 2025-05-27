package com.piltong.modudoc.client.network;

import java.net.*;
import java.io.*;

import com.piltong.modudoc.common.*;
import com.piltong.modudoc.common.document.Document;
import com.piltong.modudoc.common.document.DocumentDto;
import com.piltong.modudoc.common.operation.Operation;
import com.piltong.modudoc.common.operation.OperationDto;

// 클라이언트에서 네트워크 로직을 처리하는 클래스
public class ClientNetworkHandler implements Runnable{
    Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientNetworkHandler() {
        try {
            this.socket = new Socket(Constants.SERVER_IP, Constants.SERVER_PORT);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void run() {
        try {
            // (스트림 초기화 순서 주의) out 다음 in을 해야한다.
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 인터럽트 받기 전까지 무한 반복
        // 서버로부터 데이터 입력을 대기한다.
//        try {
//            while (!Thread.currentThread().isInterrupted()) {
//                // 작성 必
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            shutdown();
//        }



    }



    // Operation 요청 함수
    public Operation requestOperation() {
        try {
            return Operation.toEntity((OperationDto) in.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    // Operation 전송 함수
    public void sendOperation(Operation operation) {
        try {
            out.writeObject(Operation.toDto(operation));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // Document 요청 함수
    public Document requestDocument() {
        try {
            return Document.toEntity((DocumentDto) in.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    // 핸들러 종료 함수
    public void shutdown() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

        } catch (IOException ignored) {}
    }
}
