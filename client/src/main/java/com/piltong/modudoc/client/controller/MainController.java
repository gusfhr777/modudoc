package com.piltong.modudoc.client.controller;


import com.piltong.modudoc.client.model.Document;
import com.piltong.modudoc.client.network.NetworkHandler;
import com.piltong.modudoc.client.service.NetworkListenerImpl;
import com.piltong.modudoc.client.view.*;
import com.piltong.modudoc.common.Constants;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

// MVC 구조에서 중앙 컨트롤러 클래스
public class MainController {

    private static final Logger log = LogManager.getLogger(MainController.class);
    // 주요 필드
    private final Stage stage;
    private LoginController loginController; // 첫 로그인 화면 제어
    private EditorController editorController; // 편집기 화면 제어
    private DashboardController dashboardController; // 대시보드(메인화면) 제어
    private NetworkHandler networkHandler;


    // 필드 Getter
    public LoginController getLoginController() {
        return loginController;
    }

    public EditorController getEditorController() {
        return editorController;
    }

    public DashboardController getDashboardController() {
        return dashboardController;
    }





    
    // 생성자
    public MainController(Stage stage) {
        this.stage = stage;
        try {
            NetworkListenerImpl networkListener = new NetworkListenerImpl(this);
            this.networkHandler = new NetworkHandler("localhost", 4433, networkListener);
            new Thread(this.networkHandler).start();
            networkListener.setNetworkHandler(this.networkHandler); // 의존성 주입

            this.loginController = new LoginController(this, networkHandler);
            this.dashboardController = new DashboardController(this, networkHandler);
            this.editorController = new EditorController(this, networkHandler);

            // 로그인 씬
            showLogin();
        } catch (RuntimeException e) {
            log.fatal("MainController initialize Failed.");
            showFatalErrorAndExit("MainController initialize Failed.");
        }

    }







    private void showFatalErrorAndExit(String message) {
        if (Constants.DEBUG) Platform.exit();
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("치명적인 오류");
            alert.setHeaderText("애플리케이션 실행 실패");
            alert.setContentText(message);
            alert.showAndWait();
            Platform.exit();
        });

    }





    // 로그인 씬 활성화
    public void showLogin() {
        try {
            stage.setScene(new Scene(loginController.getView(), 300, 200));
            log.info("Login Open");
        } catch (RuntimeException e) {
            String errMsg = "show Login Failed.";
            log.fatal(errMsg);
            showFatalErrorAndExit(errMsg);
        }
    }

    // 대시보드 씬 활성화
    public void showDashboard() {
        try {
            stage.getScene().setRoot(dashboardController.getView());
            log.info("Dashboard Open");
        } catch (RuntimeException e) {
            String errMsg = "show Dashboard Failed.";
            log.fatal(errMsg);
            showFatalErrorAndExit(errMsg);
        }


    }

    // 에디터 씬 활성화
    public void showEditor(Document document) {
        editorController.setDocument(document);
        stage.getScene().setRoot(editorController.getView());
        log.info("Editor Open");

    }























//    // 뷰, 하위 컨트롤러
//    private MainView mainView;
//
//
//    private final Stage stage; // 현재 스테이지
//
//
//    public View getMainView() {
//        return mainView;
//    }
//
//    public LoginController getLoginController() {
//        return loginController;
//    }
//
//    public EditorController getEditorController() {
//        return editorController;
//    }
//
//    public DashboardController getDashboardController() {
//        return dashboardController;
//    }
//
//    public NetworkListenerImpl getNetworkService() {
//        return networkController;
//    }
//
//
//
//
//
//    // 컨트롤러 생성자. 초기화 담당
////    public MainController(NetworkHandler networkHandler) {
////        this.networkHandler = networkHandler;
////        // 하위 컨트롤러, 서비스 초기화
////        networkController = new NetworkListenerImpl(this);
////        loginController = new LoginController(this);
////        dashboardController = new DashboardController(this);
////        editorController = new EditorController(this);
////    }
//
//
//    // 뷰 의존성 주입
//    public void setView(View mainView) {
//        this.mainView = (MainView) mainView;
//        this.loginController.setView(mainView);
//        this.dashboardController.setView(mainView);
//        this.editorController.setView(mainView);
//        this.loginController.setView(mainView);
//    }
//
//
//
//
//    // 로그인 Scene
//    public void showLogin() {
//        stage.setScene()
//        stage.show();
//    }
//
//    // 대시보드 시작
//    public void startDashboard() {
//        currentController.end();
//
//        currentController = dashboardController;
//        dashboardController.init();
//        dashboardController.start();
//
//    }
//
//    // 에디터 시작
//    public void startEditor(Document document) {
//        currentController.end();
//
//        currentController = editorController;
//        editorController.init(document);
//        editorController.start();
//    }
//
//    // 시작
//    public void start() {
//        if (Constants.DEBUG) {
//            networkController.connect("localhost", 4433);
//            currentController = dashboardController;
//            dashboardController.init();
//            currentController.start(); // 하위 컨트롤러 시작
//        } else {
//            startLogin(); // 로그인 컨트롤러 시작
//        }
//    }
//
//    // 끝
//    public void end() {
//
//    }
//
//    // 종료
//    public void shutdown() {
//        loginController.shutdown();
//        dashboardController.shutdown();
//        editorController.shutdown();
//        networkController.shutdown();
//        mainView.shutdown();
//        System.exit(0);
//    }

}
