package com.piltong.modudoc.client.view;

import com.piltong.modudoc.client.controller.DocumentListController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

//문서를 생성할 때 생성창을 만드는 클래스
public class EditDocumentView {

    GridPane grid = new GridPane();
    Label nameLabel = new Label();
    TextField nameField = new TextField();
    Label promptLabel = new Label();
    Button editButton = new Button();

    Stage createStage = new Stage();

    DocumentListController controller;
    public EditDocumentView(DocumentListController controller) {
        this.controller = controller;
    }
    public void initialize() {
        initLayout();
        initListeners();
    }
    void initLayout() {
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(editButton, 0, 1);
        grid.add(promptLabel, 1, 1);
        grid.setHgap(10);
        Scene scene = new Scene(grid, 300, 60);
        createStage.setScene(scene);
    }

    //모든 이벤트 처리
    void initListeners() {
        editButton.setOnAction(e -> {
            //생성 버튼을 입력했을 때 이벤트

        });
        nameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                //엔터를 입력했을 때 이벤트

            }
        });

    }

    //화면 출력
    void showView() {
        createStage.show();
    }

    //화면 종료
    public void closeView() {
        createStage.close();
    }

    //화면 창 제목 정하기
    public void setStageTitle(String title) {
        createStage.setTitle(title);
    }

    public void setButtonText(String text) {
        editButton.setText(text);
    }

    //작성한 문서 제목 가져오기
    public String getDocumentTitle() {
        return nameField.getText();
    }

    //텍스트 필드의 문자 설정
    public void setDocumentTitle(String title) {
        nameField.setText(title);
    }

    //텍스트필드 밑 안내 문구 설정
    public void setPrompt(String prompt) {
        promptLabel.setText(prompt);
    }

}

