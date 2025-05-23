package com.piltong.modudoc.server.network;


import com.piltong.modudoc.common.Constants;

import java.io.IOException;
import java.net.*;

// 서버측에서 외부와의 통신을 처리하는 클래스
// 서버에서 소켓, 통신 내용은 모두 이 클래스에서 처리한다.
// 클라와의 데이터 송수신, 내부 로직에 데이터 전달, 수정 사항 반영 등
public class ServerNetworkHandler implements Runnable {
    private final int port;
    ServerSocket serverSocket;

    public ServerNetworkHandler(int port) {
        this.port = port;
        // 서버 소켓 초기화
        try {
            serverSocket = new ServerSocket(Constants.SERVER_PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    @Override
    public void run() {

    }
}
