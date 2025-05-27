package com.piltong.modudoc.client.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

//문서를 생성할 때 생성창을 만드는 클래스
class CreateDocumentView {

    GridPane grid = new GridPane();
    Label nameLabel = new Label("파일 이름");
    TextField nameField = new TextField();
    Label promptLabel = new Label("");
    Button createButton = new Button("파일 생성");

    Stage createStage = new Stage();

    public CreateDocumentView() {

    }
    void initLayout() {
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(createButton, 0, 1);
        grid.add(promptLabel, 1, 1);
        grid.setHgap(10);
        Scene scene = new Scene(grid, 300, 60);
        createStage.setScene(scene);
        createStage.setTitle("생성");
    }

    //모든 이벤트 처리
    void initListeners() {
        createButton.setOnAction(e -> {
            //생성 버튼을 입력했을 때 이벤트

        });
        nameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                //엔터를 입력했을 때 이벤트

            }
        });

    }

    void showView() {
        createStage.show();
    }

    public String getTitle() {
        return nameField.getText();
    }
    public void setTitle(String title) {
        nameField.setText(title);
    }
    public void setPrompt(String prompt) {
        promptLabel.setText(prompt);
    }
    public void closeView() {
        createStage.close();
    }
}

