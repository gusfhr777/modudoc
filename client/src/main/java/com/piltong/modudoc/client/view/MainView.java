package com.piltong.modudoc.client.view;

import com.piltong.modudoc.client.controller.*;
import javafx.stage.Stage;

public class MainView {



    // 하위 뷰
    private final LoginView loginView; // 로그인 화면
    private final DashboardView dashboardView; // 대시보드 화면
    private final DocCreateView docCreateView; // 문서 생성 화면
    private final EditorView editorView; // 문서 편집 화면

    // 스테이지
    private Stage currentStage;


    // 메인 컨트롤러
    private MainController mainController;

    // 하위 컨트롤러
//    private LoginController loginController; // 첫 로그인 화면 제어
//    private EditorController editorController; // 편집기 화면 제어
//    private DashboardController dashboardController; // 대시보드(메인화면) 제어


    // 뷰 Getter
    public LoginView getLoginView() {
        return loginView;
    }

    public DashboardView getDashboardView() {
        return dashboardView;
    }

    public DocCreateView getDocCreateView() {
        return docCreateView;
    }

    public EditorView getEditorView() {
        return editorView;
    }


    public MainView() {
        loginView = new LoginView();
        dashboardView = new DashboardView();
        docCreateView = new DocCreateView();
        editorView = new EditorView();

    }

    public void setController(MainController mainController) {
        this.mainController = mainController;
//        this.loginController = mainController.getLoginController();
//        this.editorController = mainController.getEditorController();
//        this.dashboardController = mainController.getDashboardController();
    }

    // View 시작 함수.
    public void start() {
        this.loginView.initialize(); // 로그인 뷰 준비
        this.currentStage = this.loginView.getStage(); // 로그인 스테이지 획득
        this.currentStage.show(); // 로그인 뷰 시작
    }

    public void show() {
        this.currentStage.show();
    }

    public void close() {
        this.currentStage.close();
    }
    

    public void showLoginView() {

    }

    public void showDashboardView() {

    }

    public void showEditorView() {

    }

}
