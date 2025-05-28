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



    // 클라이언트 네트워크 핸들러의 메인 로직
    // 스레드를 통해 실행할 때, 처음 실행되는 지점이다.
    // 오브젝트 인풋에 대해 다룬다.
    @Override
    public void run() {
        try {
            // 스트림 초기화 순서 주의 : out 다음 in을 해야한다.
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            // 인터럽트 받기 전까지 무한 반복
            while (!Thread.currentThread().isInterrupted()) {
                Object msg = in.readObject(); // 오브젝트 읽기

                if (msg instanceof OperationDto) { // OperationDTO 형식인 경우

                } else if (msg instanceof DocumentDto) { // DocumentDTO 형식인 경우

                }

            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);

        } finally { // 핸들러 처리 이후
            shutdown(); // 종료
        }

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
            out.writeObject(Operation.toDto(operation)); // 전송 버퍼에 로드
            out.flush(); // 전송
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
