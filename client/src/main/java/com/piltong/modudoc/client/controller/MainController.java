package com.piltong.modudoc.client.controller;


import com.piltong.modudoc.client.view.*;
import com.piltong.modudoc.client.model.*;
import com.piltong.modudoc.common.Constants;

// MVC 구조에서 중앙 컨트롤러 클래스
public class MainController {

    /**
     * 필드 영역
     */


//    private User user; // 유저 객체
    private Document document; // 문서 객체

    // 하위 컨트롤러
    private final LoginController loginController; // 첫 로그인 화면 제어
    private final EditorController editorController; // 편집기 화면 제어
    private final NetworkController networkController; // 네트워크 제어
    private final DashboardController dashboardController; // 대시보드(메인화면) 제어

    // 메인 뷰
    private MainView view;

    // 하위 뷰
    private LoginScene loginScene; // 로그인 화면
    private DashboardScene dashboardScene; // 대시보드 화면
    private DocCreateScene docCreateScene; // 문서 생성 화면
    private EditorScene editorScene; // 문서 편집 화면









    // 컨트롤러 Getter
    public LoginController getLoginController() {
        return loginController;
    }

    public EditorController getEditorController() {
        return editorController;
    }

    public NetworkController getNetworkController() {
        return networkController;
    }

    public DashboardController getDashboardController() {
        return dashboardController;
    }



    // 컨트롤러 생성자
    public MainController() {
        // 하위 컨트롤러 생성
        loginController = new LoginController();
        editorController = new EditorController();
        networkController = new NetworkController();
        dashboardController = new DashboardController();

        if (Constants.DEBUG) {
            loginController.connect("localhost", "4433");
        }


    }

    public void setView(MainView view) {
        this.view = view;
        this.loginScene = view.getLoginScene();
        this.dashboardScene = view.getDashboardScene();
        this.docCreateScene = view.getDocCreateScene();
        this.editorScene = view.getEditorScene();
    }

    public void startApp() {

    }
}
