package com.piltong.modudoc.client.view;


import com.piltong.modudoc.client.controller.LoginController;


import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



// 로그인 화면 출력을 다루는 뷰. 생성자 -> setController -> initialize -> showView -> closeView의 생명 주기를 갖는다.
public class LoginView{


    GridPane serverGrid = new GridPane();
    GridPane userGrid = new GridPane();
    GridPane consoleGrid = new GridPane();
    Separator separator = new Separator(Orientation.HORIZONTAL);

    Label nameLabel = new Label("서버 주소");
    TextField nameField = new TextField();
    Label portLabel = new Label("서버 포트");
    TextField portField = new TextField();
    Button loginButton = new Button("로그인");
    Label promptLabel = new Label("");

    Label useridLabel = new Label("아이디");
    TextField useridField = new TextField();
    Label passwordLabel = new Label("비밀번호");
    PasswordField passwordField = new PasswordField();


    VBox root = new VBox(10,
            new Label("Login"),
            useridField,
            passwordField,
            loginButton,
            promptLabel
    );


    public LoginView() {
        root.setAlignment(Pos.CENTER);
    }

    public Parent getRoot() {
        return root;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public String getUserId() {
        return useridField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public void setPrompt(String prompt) {
        promptLabel.setText(prompt);
    }





//
//    // 초기화 -> show를 위한 준비
////    public void LoginView() {
////        initLayout();
////        initListeners();
////
////    }
//
//    //구성요소들을 배치하는 메소드
//    public void initLayout() {
//        serverGrid.add(nameLabel, 0, 0);
//        serverGrid.add(nameField, 1, 0);
//        serverGrid.add(portLabel, 0, 1);
//        serverGrid.add(portField, 1, 1);
//        userGrid.add(useridLabel, 0, 3);
//        userGrid.add(useridField, 1, 3);
//        userGrid.add(passwordLabel, 0, 4);
//        userGrid.add(passwordField, 1, 4);
//        consoleGrid.add(connectButton, 0, 5);
//        consoleGrid.add(promptLabel, 1, 5);
//        serverGrid.setHgap(10);
//        userGrid.setHgap(10);
//
//        vbox.getChildren().addAll(serverGrid,separator, userGrid,consoleGrid);
//
//        Scene scene = new Scene(vbox, 400, 120);
//        startStage.setScene(scene);
//        startStage.setTitle("접속");
//
//    }
//
//    // 시작
//    public void start() {
//        startStage.show();
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
//    //모든 이벤트의 리스너를 할당하는 메소드
//    public void initListeners(LoginController loginController) {
//        connectButton.setOnAction(e -> {
//            //접속 버튼을 입력했을 때 이벤트
//            loginController.connect(getIPText(),getPortText());
//        });
//        nameField.setOnKeyPressed(e -> {
//            if (e.getCode() == KeyCode.ENTER) {
//                //엔터를 입력했을 때 이벤트
//                loginController.connect(getIPText(),getPortText());
//            }
//        });
//        portField.setOnKeyPressed(e -> {
//            if (e.getCode() == KeyCode.ENTER) {
//                loginController.connect(getIPText(), getPortText());
//            }
//        });
//        startStage.setOnCloseRequest(e ->
//                Platform.exit());
//
//    }
//
//
//    //텍스트 입력 칸의 텍스트를 반환한다
//    public String getIPText() {
//        return nameField.getText();
//    }
//
//    //텍스트 입력 칸에 텍스트를 설정한다.
//    public void setIPText(String text) {
//        nameField.setText(text);
//    }
//
//    public int getPortText() {
//        return Integer.parseInt(portField.getText());
//    }
//    public void setPortText(String text) {portField.setText(text);}
//
//    //텍스트 칸 밑에 설명 텍스트를 설정한다.
//    public void setPromptText(String text) {
//            promptLabel.setText(text);
//        }
//
//    public Stage getStage() {
//        return startStage;
//    }
}
