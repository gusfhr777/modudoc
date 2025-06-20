package com.piltong.modudoc.client.view;

import com.piltong.modudoc.client.controller.*;
import org.fxmisc.richtext.model.EditableStyledDocument;

public class MainView {



    // 하위 뷰
    private final LoginScene loginScene; // 로그인 화면
    private final DashboardScene dashboardScene; // 대시보드 화면
    private final DocCreateScene docCreateScene; // 문서 생성 화면
    private final EditorScene editorScene; // 문서 편집 화면

    // 메인 컨트롤러
    private MainController controller;

    // 하위 컨트롤러
    private LoginController loginController; // 첫 로그인 화면 제어
    private EditorController editorController; // 편집기 화면 제어
    private NetworkController networkController; // 네트워크 제어
    private DashboardController dashboardController; // 대시보드(메인화면) 제어


    // 뷰 Getter
    public LoginScene getLoginScene() {
        return loginScene;
    }

    public DashboardScene getDashboardScene() {
        return dashboardScene;
    }

    public DocCreateScene getDocCreateScene() {
        return docCreateScene;
    }

    public EditorScene getEditorScene() {
        return editorScene;
    }


    public MainView() {
        loginScene = new LoginScene();
        dashboardScene = new DashboardScene();
        docCreateScene = new DocCreateScene();
        editorScene = new EditorScene();

    }

    public void setController(MainController controller) {
        this.controller = controller;
        this.loginController = controller.getLoginController();
        this.editorController = controller.getEditorController();
        this.networkController = controller.getNetworkController();
        this.dashboardController = controller.getDashboardController();

    }

    public void show() {

    }
}
