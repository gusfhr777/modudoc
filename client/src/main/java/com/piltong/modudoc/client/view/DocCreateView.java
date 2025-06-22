package com.piltong.modudoc.client.view;


import com.piltong.modudoc.client.model.*;

import com.piltong.modudoc.client.controller.DashboardController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

//문서를 생성할 때 생성창을 만드는 클래스
public class DocCreateView {

//    // 컨트롤러
//
//
//    // UI 객체
//    GridPane grid = new GridPane();
//    Label nameLabel = new Label();
//    TextField nameField = new TextField();
//    Label promptLabel = new Label();
//    Button editButton = new Button();
//
//    Stage editStage = new Stage();
//
//    Document summary;
//
//
//
//    // 시작
//    public void start() {
//        editStage.show();
//    }
//
//    // 끝
//    public void end() {
//        editStage.close();
//
//    }
//
//    // 종료
//    public void shutdown() {
//
//    }
//
//
//
//
//
//
//
//
//
//
//    //    public DocCreateView(DashboardController controller, Document summary) {this.controller = controller;
////    this.summary = summary;}
//    public void initialize(DashboardController controller) {
//        initLayout();
//        initListeners(controller);
//    }
//
//    void initLayout() {
//        grid.add(nameLabel, 0, 0);
//        grid.add(nameField, 1, 0);
//        grid.add(editButton, 0, 1);
//        grid.add(promptLabel, 1, 1);
//        grid.setHgap(10);
//        Scene scene = new Scene(grid, 300, 60);
//        editStage.setScene(scene);
//    }
//
//    //모든 이벤트 처리
//    void initListeners(DashboardController controller) {
//        editButton.setOnAction(e -> {
//            //생성 버튼을 입력했을 때 이벤트
//            returnEdit(controller);
//        });
//        nameField.setOnKeyPressed(e -> {
//            if (e.getCode() == KeyCode.ENTER) {
//                //엔터를 입력했을 때 이벤트
//                returnEdit(controller);
//            }
//        });
//        editStage.setOnCloseRequest(e->{
//            controller.setIsEditing(false);
//        });
//
//    }
//
//    public void setDocument(Document document) {
//        summary = document;
//        setDocumentTitle(summary.getTitle());
//    }
//
//    public void returnEdit(DashboardController controller) {
//        if (!nameField.getText().isEmpty()) {
//            if(summary == null) {
//                controller.sendCreateDocument(nameField.getText());
//                controller.setIsEditing(false);
//                closeView();
//            }else {
//                Document oldSummary = summary;
//                summary.setTitle(nameField.getText());
//                controller.sendEditDocument(oldSummary,summary);
//                controller.setIsEditing(false);
//                closeView();
//            }
//        }
//        else {
//            setPrompt("문서 제목을 입력해주세요");
//        }
//    }
//    //화면 출력
//    public void showView() {
//        editStage.show();
//    }
//
//    //화면 종료
//    public void closeView() {
//        editStage.close();
//    }
//
//    //화면 창 제목 정하기
//    public void setStageTitle(String title) {
//        editStage.setTitle(title);
//    }
//
//    public void setButtonText(String text) {
//        editButton.setText(text);
//    }
//
//    //작성한 문서 제목 가져오기
//    public String getDocumentTitle() {
//        return nameField.getText();
//    }
//
//    //텍스트 필드의 문자 설정
//    public void setDocumentTitle(String title) {
//        nameField.setText(title);
//    }
//
//    //텍스트필드 밑 안내 문구 설정
//    public void setPrompt(String prompt) {
//        promptLabel.setText(prompt);
//    }

}

