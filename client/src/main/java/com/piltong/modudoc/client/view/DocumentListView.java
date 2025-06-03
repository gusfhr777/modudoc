package com.piltong.modudoc.client.view;

import com.piltong.modudoc.common.document.Document;
import com.piltong.modudoc.client.controller.DocumentListController;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DocumentListView {
<<<<<<< Updated upstream
    DocumentListController controller;

    TableView<Document> documentTable = new TableView<>(); //문서 목록이 표시될 도표

    //도표에 첫번째 행에 각 열의 정보 표시
=======
    TableView<Document> documentTable = new TableView<>();
>>>>>>> Stashed changes
    TableColumn<Document, String> titleColumn = new TableColumn<>("파일 이름");
    TableColumn<Document, String> createdDateColumn = new TableColumn<>("생성 일자");
    TableColumn<Document, String> modifiedDateColumn = new TableColumn<>("수정 일자");
    TableColumn<Document, Integer> usercountColumn = new TableColumn<>("접속 인원");
    VBox vBox = new VBox();
    HBox hBox = new HBox();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    Button createButton = new Button("파일 생성");
    Button InButton = new Button("파일 접속");
    Button DeleteButton = new Button("파일 삭제");
    DocumentListView() {
        initComponents();
        initLayout();
        initListeners();
    }

<<<<<<< Updated upstream
    Stage DocumentListStage = new Stage();

    //컨트롤러 설정
    public void setController(DocumentListController controller) {
        this.controller = controller;
    }

    //각 구성요소들을 초기화하는 메소드
    public void initComponents() {
=======
    void initComponents() {
>>>>>>> Stashed changes
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        createdDateColumn.setCellValueFactory(celldata ->{
            LocalDateTime time = celldata.getValue().getCreatedDate();
            return new SimpleStringProperty(time.format(formatter));
        });
        modifiedDateColumn.setCellValueFactory(new PropertyValueFactory<>("modifiedDate"));

        TableView.TableViewSelectionModel<Document> selectionModel = documentTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

    }
<<<<<<< Updated upstream
    //각 구성요소들을 배치하는 메소드
    public void initLayout() {
        hBox.getChildren().addAll(createButton, InButton,ModifyButton, DeleteButton);
=======
    void initLayout() {
        hBox.getChildren().addAll(createButton, InButton, DeleteButton);
>>>>>>> Stashed changes
        vBox.getChildren().addAll(hBox, documentTable);
        documentTable.getColumns().addAll(titleColumn, createdDateColumn, modifiedDateColumn, usercountColumn);
    }

<<<<<<< Updated upstream
    //이벤트들을 감지하는 메소드
    public void initListeners() {
        createButton.setOnAction(e -> {
            //생성 버튼 입력시 이벤트

        });
        InButton.setOnAction(e -> {
            //접속 버튼 입력시 이벤트

        });
        ModifyButton.setOnAction(e->{
            //수정 버튼 입력시 이벤트

        });
        DeleteButton.setOnAction(e -> {
            //삭제 버튼 입력시 이벤트
=======
    void initListeners() {
        createButton.setOnAction(e -> {

        });
        InButton.setOnAction(e -> {

        });
        DeleteButton.setOnAction(e -> {
>>>>>>> Stashed changes

        });
    }

<<<<<<< Updated upstream


    public void viewShow() {
        DocumentListStage.show();
    }
    public void viewClose() {
        DocumentListStage.close();
    }

    //목록에 문서를 추가하는 메소드
    public void addDocument(Document document) {
        documentTable.getItems().add(document);
    }
    //목록에 문서를 삭제하는 메소드
    public void removeDocument(Document document) {
        documentTable.getItems().remove(document);
    }
    //목록을 설정하는 메소드
    public void setDocumentList(List<Document> documentList) {
        documentTable.setItems((ObservableList<Document>) documentList);
    }
    //목록의 모든 문서를 제거하는 메소드
    public void clearDocumentList() {
        documentTable.getItems().clear();
    }
    //선택된 문서를 반환하는 메소드
    public Document getSelectedDocument() {
        return selectionModel.getSelectedItem();
=======
    void addDocument(Document document) {
        documentTable.getItems().add(document);
    }

    Document getSelectedDocument() {
        return documentTable.getSelectionModel().getSelectedItem();
>>>>>>> Stashed changes
    }

}


