package com.piltong.modudoc.client.ClientUI;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class StartView {
    GridPane grid = new GridPane();
    Label nameLabel = new Label("서버 주소");
    TextField nameField = new TextField();
    Button LoginButton = new Button("접속");
    Label promptLabel = new Label("");
    StartView() {
        initLayout();
        LoginButton.setOnAction(e -> {
            //접속 버튼을 입력했을 때 이벤트

        });
        nameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                //엔터를 입력했을 때 이벤트
            }
        });

    }

    void initLayout() {
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(LoginButton, 0, 1);
        grid.add(promptLabel, 1, 1);
        grid.setHgap(10);

        Stage startStage = new Stage();
        Scene scene = new Scene(grid, 300, 60);
        startStage.setScene(scene);
        startStage.setTitle("접속");
        startStage.show();
    }

    String getText() {
        return nameField.getText();
    }
    void setText(String text) {
        nameField.setText(text);
    }
    void setPromptText(String text) {
        promptLabel.setText(text);
    }
}
