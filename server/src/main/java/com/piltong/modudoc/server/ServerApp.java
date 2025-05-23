package com.piltong.modudoc.server;


import com.piltong.modudoc.common.Constants;
import com.piltong.modudoc.server.network.ServerNetworkHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// 서버 앱의 메인 클래스
// 단순히 서버 프로그램의 시작점 역할(부트스트래핑)을 하며, 핵심 비즈니스 로직은 타 컴포넌트(서비스, 핸들러, 컨트롤러 등)에 위임한다.
public class ServerApp {
    public static void main(String[] args) {
        // 시작
        System.out.println("서버 가동중");

        // ServerNNetworkHandler 생성 및 시작
        ServerNetworkHandler networkHandler = new ServerNetworkHandler(Constants.SERVER_PORT);

    }
}