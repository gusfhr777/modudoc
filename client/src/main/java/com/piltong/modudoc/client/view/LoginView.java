package com.piltong.modudoc.client.view;


import com.piltong.modudoc.client.controller.LoginController;


import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



// 로그인 화면 출력을 다루는 뷰. 생성자 -> setController -> initialize -> showView -> closeView의 생명 주기를 갖는다.
public class LoginView {

    // 컨트롤러
    LoginController loginController;


    // UI 컴포넌트
    GridPane serverGrid = new GridPane();
    GridPane userGrid = new GridPane();
    GridPane consoleGrid = new GridPane();
    VBox vbox = new VBox();
    Separator separator = new Separator(Orientation.HORIZONTAL);

    Label nameLabel = new Label("서버 주소");
    TextField nameField = new TextField();
    Label portLabel = new Label("서버 포트");
    TextField portField = new TextField();
    Button connectButton = new Button("접속");
    Label promptLabel = new Label("");

    Label useridLabel = new Label("아이디");
    TextField useridField = new TextField();
    Label passwordLabel = new Label("비밀번호");
    PasswordField passwordField = new PasswordField();


    // JavaFX Stage 객체
    Stage startStage = new Stage();


    public void setController(LoginController loginController) {
        this.loginController = loginController;
    }


    // 초기화 -> show를 위한 준비
    public void initialize() {
        initLayout();
        initListeners();
    }

    //구성요소들을 배치하는 메소드
    public void initLayout() {
        serverGrid.add(nameLabel, 0, 0);
        serverGrid.add(nameField, 1, 0);
        serverGrid.add(portLabel, 0, 1);
        serverGrid.add(portField, 1, 1);
        userGrid.add(useridLabel, 0, 3);
        userGrid.add(useridField, 1, 3);
        userGrid.add(passwordLabel, 0, 4);
        userGrid.add(passwordField, 1, 4);
        consoleGrid.add(connectButton, 0, 5);
        consoleGrid.add(promptLabel, 1, 5);
        serverGrid.setHgap(10);
        userGrid.setHgap(10);

        vbox.getChildren().addAll(serverGrid,separator, userGrid,consoleGrid);

        Scene scene = new Scene(vbox, 400, 120);
        startStage.setScene(scene);
        startStage.setTitle("접속");

    }

    //모든 이벤트의 리스너를 할당하는 메소드
    public void initListeners() {
        connectButton.setOnAction(e -> {
            //접속 버튼을 입력했을 때 이벤트
            loginController.connect(getIPText(),getPortText());
        });
        nameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                //엔터를 입력했을 때 이벤트
                loginController.connect(getIPText(),getPortText());
            }
        });
        portField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                loginController.connect(getIPText(), getPortText());
            }
        });
        startStage.setOnCloseRequest(e->
                Platform.exit());

    }


    //텍스트 입력 칸의 텍스트를 반환한다
    public String getIPText() {
        return nameField.getText();
    }

    //텍스트 입력 칸에 텍스트를 설정한다.
    public void setIPText(String text) {
        nameField.setText(text);
    }

    public int getPortText() {
        return Integer.parseInt(portField.getText());
    }
    public void setPortText(String text) {portField.setText(text);}

    //텍스트 칸 밑에 설명 텍스트를 설정한다.
    public void setPromptText(String text) {
            promptLabel.setText(text);
        }

    public Stage getStage() {
        return startStage;
    }
}
