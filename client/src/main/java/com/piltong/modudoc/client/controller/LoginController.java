package com.piltong.modudoc.client.controller;

import com.piltong.modudoc.client.model.LoginRequest;
import com.piltong.modudoc.client.network.NetworkHandler;
import com.piltong.modudoc.client.view.LoginView;
import com.piltong.modudoc.common.network.ClientCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// LoginController.java
import javafx.scene.Parent;


public class LoginController{
    private static final Logger log = LogManager.getLogger(LoginController.class);
    private final MainController mainController;
    private final NetworkHandler networkHandler;
    private final LoginView loginView;

    public LoginController(MainController mainController, NetworkHandler networkHandler) {
        this.mainController = mainController;
        this.networkHandler = networkHandler;
        this.loginView = new LoginView();

        loginView.getLoginButton().setOnAction(e -> {
            if (loginView.getUserId().isEmpty() || loginView.getPassword().isEmpty())
                setPrompt("아이디와 비밀번호를 입력해주세요");
            else {
                onLogin();
            }
        }
        );
    }

    public void onLogin() {
        LoginRequest payload = new LoginRequest(loginView.getUserId(), loginView.getPassword());
        networkHandler.sendCommand(ClientCommand.LOGIN, payload);
        log.info("Login Pressed.");
    }

    public Parent getView() {
        return this.loginView.getRoot();
    }

    public void setPrompt(String prompt) {
        loginView.setPrompt(prompt);
    }

//    // 컨트롤러, 뷰, 서비스
//    private MainController mainController;
//    private MainView mainView;
//    private LoginView loginView;
//    private networkService networkController;
//
//    //생성자. 컨트롤러 초기화 담당.
//    public LoginController(MainController mainController) {
//        this.mainController = mainController;
//    }
//
//
//    // 뷰 설정
//    public void setView(View view) {
//        this.mainView = (MainView) view;
//        this.loginView = this.mainView.getLoginView();
//    }
//
//    // 시작을 위한 초기화
//    public void init() {
//        loginView.initLayout();
//        loginView.initListeners(this);
//    }
//
//    // 시작
//    public void start() {
//        this.loginView.start();
//
//    }
//
//    // 끝
//    public void end() {
//
//    }
//
//    // 종료
//    public void shutdown() {
//
//    }
//
//    //연결 버튼을 눌렀을 때, 네트워크 연결
//    public void connect(String host, int port) {
//        networkController.connect(host, port);
//    }

}
