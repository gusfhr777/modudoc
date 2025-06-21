package com.piltong.modudoc.client.controller;

import com.piltong.modudoc.client.view.LoginView;
import com.piltong.modudoc.client.view.MainView;
import com.piltong.modudoc.client.view.View;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginController implements Controller{
    private static final Logger log = LogManager.getLogger(LoginController.class);


    // 컨트롤러, 뷰, 서비스
    private MainController mainController;
    private MainView mainView;
    private LoginView loginView;
    private NetworkController networkController;

    //생성자. 컨트롤러 초기화 담당.
    public LoginController(MainController mainController) {
        this.mainController = mainController;
    }
    


    // 뷰 설정
    public void setView(View view) {
        this.mainView = (MainView) view;
        this.loginView = this.mainView.getLoginView();
    }

    // 시작을 위한 초기화
    public void init() {
        loginView.initLayout();
        loginView.initListeners(this);
    }

    // 시작
    public void start() {
        this.loginView.start();

    }

    // 끝
    public void end() {

    }

    // 종료
    public void shutdown() {

    }








    //연결 버튼을 눌렀을 때, 네트워크 연결
    public void connect(String host, int port) {
        networkController.connect(host, port);
    }

}
