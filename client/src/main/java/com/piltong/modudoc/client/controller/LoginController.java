package com.piltong.modudoc.client.controller;

import com.piltong.modudoc.client.view.EditorView;
import com.piltong.modudoc.client.view.LoginView;
import com.piltong.modudoc.client.view.MainView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginController implements Controller{
    private static final Logger log = LogManager.getLogger(LoginController.class);


    // 컨트롤러, 뷰, 서비스
    private MainController mainController;
    private MainView mainView;
    private LoginView loginView;
    private NetworkService networkService;

    //생성자. 컨트롤러 초기화 담당.
    public LoginController(MainController mainController) {
        this.mainController = mainController;
        this.loginView = mainController.getMainView().getLoginView();
    }
    



    // 시작
    public void start() {
        this.loginView.initialize();
        this.loginView.show();

    }

    // 끝
    public void end() {

    }

    // 종료
    public void shutdown() {

    }



    //연결 버튼을 눌렀을 때, 네트워크 연결
    public void connect(String host, int port) {
        networkService.connect(host, port);
    }

}
