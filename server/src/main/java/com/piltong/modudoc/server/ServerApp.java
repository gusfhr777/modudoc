package com.piltong.modudoc.server;


import com.piltong.modudoc.common.Constants;
import com.piltong.modudoc.server.network.ServerNetworkHandler;
import com.piltong.modudoc.server.service.ServerNetworkListenerImpl;
import com.piltong.modudoc.server.service.DocumentService;
import com.piltong.modudoc.server.service.SyncService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 서버 앱의 메인 클래스
// 단순히 서버 프로그램의 시작점 역할(부트스트래핑)을 한다.
// 핵심 비즈니스 로직은 타 컴포넌트(서비스, 핸들러, 컨트롤러 등)에 위임한다.
public class ServerApp {
    public static void main(String[] args) {

        // ServerNetworkHandler 초기화

        // 스레드 풀 생성
        // 스레드 풀 : 앱에서 사용하는 스레드 개수를 관리하는 기법. https://engineerinsight.tistory.com/197
        ExecutorService executor = Executors.newFixedThreadPool(Constants.POOL_SIZE);

        // 서비스 객체 생성
        DocumentService documentService = new DocumentService();
        SyncService syncService = new SyncService();

        // listener 구현체 생성
        ServerNetworkListenerImpl listener = new ServerNetworkListenerImpl(documentService, syncService);
        System.out.println("Service started.");

        // ServerNetworkHandler 스레드 생성 및 시작
        Runnable networkHandler = new ServerNetworkHandler(Constants.SERVER_PORT, executor, listener);
        new Thread(networkHandler).start();

        // 시작
        System.out.println("Server Stared on Port " + Constants.SERVER_PORT);

    }
}