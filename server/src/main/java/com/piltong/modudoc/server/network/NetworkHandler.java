package com.piltong.modudoc.server.network;


import com.piltong.modudoc.common.Constants;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

// 서버측에서 외부와의 통신을 처리하는 클래스
// 서버에서 소켓, 통신 내용은 모두 이 클래스에서 처리한다.
// 클라와의 데이터 송수신, 내부 로직에 데이터 전달, 수정 사항 반영 등
public class NetworkHandler implements Runnable {

    private final int port; // 네트워크 핸들러가 시작되는 포트
    private final ServerSocket serverSocket; // 데이터를 관장하는 서버 소켓
    private final ExecutorService executor; // 스레드 풀
    private final networkHandlerListener listener; // 네트워크 핸들러에서 비즈니스 로직을 처리하는 객체

    private final ConcurrentHashMap<SocketAddress, ClientHandler> sessionMap = new ConcurrentHashMap<>();



    // 핸들러 초기화 함수
    public NetworkHandler(int port, ExecutorService executor, networkHandlerListener listener) {
        // 변수 할당
        this.port = port;
        this.executor = executor;
        this.listener = listener;

        // 서버 소켓 초기화
        try {
            serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    // 서버 네트워크 핸들러의 메인 로직
    // 클라로부터 데이터 송신을 대기하고, 송신을 받으면 그에 따른 로직을 처리한다.
    // 스레드를 통해 실행할 때, 처음 실행되는 지점이다.
    @Override
    public void run() {
        try {
            // 스레드가 중지되지 않을 때까지 반복한다.
            // 클라이언트 접속 무한대기
            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection from " + clientSocket.getRemoteSocketAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, listener);

                sessionMap.put(clientSocket.getRemoteSocketAddress(), clientHandler);
                executor.submit(clientHandler); // 스레드 풀에 할당한다.
            }

        } catch (IOException e) {
            throw new RuntimeException(e); // 런타임 오류 발생

        } finally {
            shutdown();
        }
    }


//    // 명령어를 클라 측으로 전송한다. 성공한 경우.
//    public <T extends Serializable> void sendCommandSuccess(ClientHandler target, ClientCommand command, T payload) {
//
//        try {
//            ResponseCommandDto<T> response = new ResponseCommandDto<>(command, payload, true, null);
//            target.out.writeObject(response);
//            target.out.flush();
//        } catch (Exception e) {
//            System.err.println("클라 전송 실패 " + target + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    // 명령어를 클라 측으로 전송한다. 실패한 경우.
//    public void sendCommandFailure(ClientHandler target, ClientCommand command, String errorMsg) {
//
//        try {
//            ResponseCommandDto<Object> response = new ResponseCommandDto<>(command, null, false, errorMsg);
//            target.out.writeObject(response);
//            target.out.flush();
//        } catch (Exception e) {
//            System.err.println("클라 전송 실패 " + target + e.getMessage());
//            e.printStackTrace();
//        }
//    }

    // 핸들러 종료 함수
    public void shutdown() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            executor.shutdownNow();

        } catch (IOException ignored) {}
    }

}
