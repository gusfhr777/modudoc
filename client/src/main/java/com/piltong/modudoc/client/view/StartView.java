package com.piltong.modudoc.client.view;


import com.piltong.modudoc.client.controller.StartController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class StartView {
    StartController controller;

    GridPane grid = new GridPane();
    Label nameLabel = new Label("서버 주소");
    TextField nameField = new TextField();
    Button connectButton = new Button("접속");
    Label promptLabel = new Label("");

    Stage startStage = new Stage();

    void setController(StartController controller) {
        this.controller = controller;
    }

    //구성요소들을 배치하는 메소드
    void initLayout() {
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(connectButton, 0, 1);
        grid.add(promptLabel, 1, 1);
        grid.setHgap(10);

        Scene scene = new Scene(grid, 300, 60);
        startStage.setScene(scene);
        startStage.setTitle("접속");

    }

    //모든 이벤트를 감지하는 메소드
    void initListeners() {
        connectButton.setOnAction(e -> {
            //접속 버튼을 입력했을 때 이벤트
            controller.connect(getText());
        });
        nameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                    //엔터를 입력했을 때 이벤트
                controller.connect(getText());
            }
        });

    }

    public void showView() {
        startStage.show();
    }

    public void closeView() {
        startStage.close();
    }

    //텍스트 입력 칸의 텍스트를 반환한다
    public String getText() {
        return nameField.getText();
    }

    //텍스트 입력 칸에 텍스트를 설정한다.
    public void setText(String text) {
        nameField.setText(text);
    }

    //텍스트 칸 밑에 설명 텍스트를 설정한다.
    public void setPromptText(String text) {
            promptLabel.setText(text);
        }
}
