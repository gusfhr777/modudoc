package com.piltong.modudoc.client.controller;


import com.piltong.modudoc.client.network.NetworkHandler;
import com.piltong.modudoc.client.view.*;
import com.piltong.modudoc.client.model.*;
import com.piltong.modudoc.common.Constants;

// MVC 구조에서 중앙 컨트롤러 클래스
public class MainController implements Controller{

//    // 모델
//    private Document document;

//    // 상태
//    private boolean isEditing = false;
//    private User user;


    // 뷰, 하위 컨트롤러, 서비스
    private MainView mainView;
    private final LoginController loginController; // 첫 로그인 화면 제어
    private final EditorController editorController; // 편집기 화면 제어
    private final DashboardController dashboardController; // 대시보드(메인화면) 제어
    private final NetworkService networkService; // 네트워크 제어


    private Controller currentController; // 현재 동작 중인 컨트롤러


    public MainView getMainView() {
        return mainView;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public EditorController getEditorController() {
        return editorController;
    }

    public DashboardController getDashboardController() {
        return dashboardController;
    }

    public NetworkService getNetworkService() {
        return networkService;
    }





    // 컨트롤러 생성자. 초기화 담당
    public MainController() {
        // 하위 컨트롤러, 서비스 초기화
        networkService = new NetworkService(this);
        loginController = new LoginController(this);
        dashboardController = new DashboardController(this);
        editorController = new EditorController(this);
    }


    // 뷰 의존성 주입
    public void setView(MainView mainView) {
        this.mainView = mainView;
    }



    // 시작
    public void start() {
        if (Constants.DEBUG) {
            networkService.connect("localhost", 4433);
            currentController = dashboardController;
        } else {
            currentController = loginController; // 로그인 컨트롤러로 시작
        }

        currentController.start(); // 하위 컨트롤러 시작
    }

    // 끝
    public void end() {

    }

    // 종료
    public void shutdown() {

    }





}
