package com.piltong.modudoc.client.view;

import com.piltong.modudoc.client.controller.*;
import javafx.stage.Stage;

public class MainView implements View{


    // 하위 뷰
    private final LoginView loginView; // 로그인 화면
    private final DashboardView dashboardView; // 대시보드 화면
    private final DocCreateView docCreateView; // 문서 생성 화면
    private final EditorView editorView; // 문서 편집 화면

    // 현재 뷰
    private View currentView;





    // Getter
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

    public View getCurrentView() {
        return currentView;
    }








    // 초기화
    public MainView() {
        loginView = new LoginView();
        dashboardView = new DashboardView();
        docCreateView = new DocCreateView();
        editorView = new EditorView();

    }



    // 시작
    public void start() {


//        this.loginView.initialize(); // 로그인 뷰 준비x   x
//        this.currentView = this.loginView; // 로그인 스테이지 획득
//        this.currentView.show(); // 로그인 뷰 시작
//    }
    }
    // 끝
    public void end() {

    }

    // 종료
    public void shutdown() {

    }

//    public void show() {
//        this.currentView.show();
//    }
//
//    public void close() {
//        this.currentView.close();
//    }
    

    public void showLoginView() {

    }

    public void showDashboardView() {

    }

    public void showEditorView() {

    }

}
