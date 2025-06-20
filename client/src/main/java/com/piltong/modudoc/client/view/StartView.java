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
    Label portLabel = new Label("서버 포트");
    TextField portField = new TextField();
    Button connectButton = new Button("접속");
    Label promptLabel = new Label("");

    Stage startStage = new Stage();

    public void setController(StartController controller) {
        this.controller = controller;
    }

    public void initialize() {
        initLayout();
        initListeners();
    }

    //구성요소들을 배치하는 메소드
    public void initLayout() {
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(portLabel, 0, 1);
        grid.add(portField, 1, 1);
        grid.add(connectButton, 0, 2);
        grid.add(promptLabel, 1, 2);
        grid.setHgap(10);

        Scene scene = new Scene(grid, 300, 70);
        startStage.setScene(scene);
        startStage.setTitle("접속");

    }

    //모든 이벤트를 감지하는 메소드
    public void initListeners() {
        connectButton.setOnAction(e -> {
            //접속 버튼을 입력했을 때 이벤트
            controller.connect(getIPText(),getPortText());
        });
        nameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                //엔터를 입력했을 때 이벤트
                controller.connect(getIPText(),getPortText());
            }
        });
        portField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                controller.connect(getIPText(), getPortText());
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
    public String getIPText() {
        return nameField.getText();
    }

    //텍스트 입력 칸에 텍스트를 설정한다.
    public void setIPText(String text) {
        nameField.setText(text);
    }

    public String getPortText() {return portField.getText();}
    public void setPortText(String text) {portField.setText(text);}

    //텍스트 칸 밑에 설명 텍스트를 설정한다.
    public void setPromptText(String text) {
            promptLabel.setText(text);
        }
}
