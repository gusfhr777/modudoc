package com.piltong.modudoc.client.view;


import com.piltong.modudoc.client.controller.LoginController;


import javafx.application.Platform;
import javafx.geometry.Insets;
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

    Button loginButton = new Button("로그인");
    Label promptLabel = new Label("");

    TextField useridField = new TextField();
    PasswordField passwordField = new PasswordField();


    VBox root = new VBox(10,
            new Label("모두문서 로그인"),
            useridField,
            passwordField,
            loginButton,
            promptLabel
    );


    public LoginView() {
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #F8FBFF");

        useridField.setPromptText("아이디를 입력하세요");
        passwordField.setPromptText("비밀번호를 입력하세요");

        useridField.setMaxWidth(220);
        passwordField.setMaxWidth(220);
        loginButton.setStyle(
                "-fx-background-color: #42A5F5;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8px;" +
                "-fx-padding: 8 16;"
        );

        String defaultStyle = "-fx-background-color: #42A5F5;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5px;" +
                "-fx-padding: 8 16;";
        String hoverStyle = "-fx-background-color: #1E88E5;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5px;" +
                "-fx-padding: 8 16;";
        loginButton.setStyle(defaultStyle);
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(hoverStyle));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(defaultStyle));
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


}
