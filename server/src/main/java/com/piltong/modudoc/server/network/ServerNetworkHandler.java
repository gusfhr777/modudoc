package com.piltong.modudoc.server.network;


import com.piltong.modudoc.common.Constants;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;

// 서버측에서 외부와의 통신을 처리하는 클래스
// 서버에서 소켓, 통신 내용은 모두 이 클래스에서 처리한다.
// 클라와의 데이터 송수신, 내부 로직에 데이터 전달, 수정 사항 반영 등
public class ServerNetworkHandler implements Runnable {
    private final int port;
    private final ExecutorService executor;
    ServerSocket serverSocket;


    // 핸들러 초기화 함수
    public ServerNetworkHandler(int port, ExecutorService executor) {
        // 변수 할당
        this.port = port;
        this.executor = executor;
        
        // 서버 소켓 초기화
        try {
            serverSocket = new ServerSocket(Constants.SERVER_PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    // 핸들러 메인 로직
    @Override
    public void run() {
        boolean running = true; // 네트워크 핸들러 루프

        try {
            // 스레드가 중지되지 않을 때까지 반복한다.
            // 클라이언트 접속 무한대기
            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection from " + clientSocket.getRemoteSocketAddress());

                Runnable clientHandler = new ClientHandler(clientSocket);
                executor.submit(clientHandler); // 스레드 풀에 할당한다.
            }

        } catch (IOException e) {
            throw new RuntimeException(e); // 런타임 오류 발생

        } finally {
            shutdown();
        }
    }


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
