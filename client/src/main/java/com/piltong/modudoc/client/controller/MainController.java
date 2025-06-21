package com.piltong.modudoc.client.controller;


import com.piltong.modudoc.client.model.Document;
import com.piltong.modudoc.client.view.*;
import com.piltong.modudoc.common.Constants;

// MVC 구조에서 중앙 컨트롤러 클래스
public class MainController implements Controller{

//    // 모델
//    private Document document;

//    // 상태
//    private boolean isEditing = false;
//    private User user;


    // 뷰, 하위 컨트롤러
    private MainView mainView;
    private final LoginController loginController; // 첫 로그인 화면 제어
    private final EditorController editorController; // 편집기 화면 제어
    private final DashboardController dashboardController; // 대시보드(메인화면) 제어
    private final NetworkController networkController; // 네트워크 제어


    private Controller currentController; // 현재 동작 중인 컨트롤러


    public View getMainView() {
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

    public NetworkController getNetworkService() {
        return networkController;
    }





    // 컨트롤러 생성자. 초기화 담당
    public MainController() {
        // 하위 컨트롤러, 서비스 초기화
        networkController = new NetworkController(this);
        loginController = new LoginController(this);
        dashboardController = new DashboardController(this);
        editorController = new EditorController(this);
    }


    // 뷰 의존성 주입
    public void setView(View mainView) {
        this.mainView = (MainView) mainView;
        this.loginController.setView(mainView);
        this.dashboardController.setView(mainView);
        this.editorController.setView(mainView);
        this.loginController.setView(mainView);
    }




    // 로그인 시작
    public void startLogin() {
        currentController = loginController;
        loginController.init();
        loginController.start();
    }

    // 대시보드 시작
    public void startDashboard() {
        currentController.end();

        currentController = dashboardController;
        dashboardController.init();
        dashboardController.start();

    }

    // 에디터 시작
    public void startEditor(Document document) {
        currentController.end();

        currentController = editorController;
        editorController.init(document);
        editorController.start();
    }

    // 시작
    public void start() {
        if (Constants.DEBUG) {
            networkController.connect("localhost", 4433);
            currentController = dashboardController;
            dashboardController.init();
            currentController.start(); // 하위 컨트롤러 시작
        } else {
            startLogin(); // 로그인 컨트롤러 시작
        }
    }

    // 끝
    public void end() {

    }

    // 종료
    public void shutdown() {
        loginController.shutdown();
        dashboardController.shutdown();
        editorController.shutdown();
        networkController.shutdown();
        mainView.shutdown();
        System.exit(0);
    }

}
