package com.piltong.modudoc.server;


import com.piltong.modudoc.common.Constants;
import com.piltong.modudoc.server.model.Document;
import com.piltong.modudoc.server.network.NetworkHandler;
import com.piltong.modudoc.server.repository.DocumentRepository;
import com.piltong.modudoc.server.repository.JDBCDocumentRepository;
import com.piltong.modudoc.server.repository.MapDocumentRepository;
import com.piltong.modudoc.server.service.networkService;
import com.piltong.modudoc.server.service.DocumentService;
import com.piltong.modudoc.server.service.SyncService;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// 서버 앱의 메인 클래스. 서버 프로그램의 시작점이다.
// 서버 프로그램을 구동하는 역할(부트스트래핑)을 한다.
// 비즈니스 로직은 타 컴포넌트(서비스, 핸들러, 컨트롤러 등)에 위임한다.
public class ServerApp {
    // 로거 생성
    private static final Logger log = LogManager.getLogger(ServerApp.class);

    public static void main(String[] args) {

        log.info("ServerApp Initializing.");


        try {

            // 스레드 풀 생성
            // 스레드 풀 : 앱에서 사용하는 스레드 개수를 관리하는 기법. https://engineerinsight.tistory.com/197
            ExecutorService executor = Executors.newFixedThreadPool(Constants.POOL_SIZE);

            // 서비스 객체 생성
//            DocumentRepository docRepo = new JDBCDocumentRepository();

            DocumentRepository docRepo;


            if (Constants.DEBUG) { // 문서 테스트용 : 추후 삭제
                docRepo = new MapDocumentRepository();
                docRepo.save(new Document(1, "DocumentTest1", "c1", LocalDateTime.now(), LocalDateTime.now()));
                docRepo.save(new Document(2, "DocumentTest2", "c2", LocalDateTime.now(), LocalDateTime.now()));

            } else {
                docRepo = new JDBCDocumentRepository();
            }


            DocumentService docService = new DocumentService(docRepo);
            SyncService syncService = new SyncService(docService);
            networkService listener = new networkService(docService, syncService);
            log.info("Service started.");

            // ServerNetworkHandler 스레드 생성 및 시작
            Runnable networkHandler = new NetworkHandler(Constants.SERVER_PORT, executor, listener);
            new Thread(networkHandler).start();
            log.info("Network handler started.");


        } catch (Exception e) {
            log.error("Error while Executing Service : ", e);
            return; // 치명적 오류 후 종료
        }



        log.info("ServerApp successfully initialized on port {}", Constants.SERVER_PORT);

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            log.error("Main thread interrupted", e);
        }
    }
}