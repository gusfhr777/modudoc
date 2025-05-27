package com.piltong.modudoc.client.ClientUI;

import com.piltong.modudoc.common.document.Document;

import javafx.beans.property.SimpleStringProperty;
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

public class DocumentListView {
    TableView<Document> documentTable = new TableView<>();
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

    void initComponents() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        createdDateColumn.setCellValueFactory(celldata ->{
            LocalDateTime time = celldata.getValue().getCreatedDate();
            return new SimpleStringProperty(time.format(formatter));
        });
        modifiedDateColumn.setCellValueFactory(new PropertyValueFactory<>("modifiedDate"));

        TableView.TableViewSelectionModel<Document> selectionModel = documentTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

    }
    void initLayout() {
        hBox.getChildren().addAll(createButton, InButton, DeleteButton);
        vBox.getChildren().addAll(hBox, documentTable);
        documentTable.getColumns().addAll(titleColumn, createdDateColumn, modifiedDateColumn, usercountColumn);
    }

    void initListeners() {
        createButton.setOnAction(e -> {

        });
        InButton.setOnAction(e -> {

        });
        DeleteButton.setOnAction(e -> {

        });
    }

    void addDocument(Document document) {
        documentTable.getItems().add(document);
    }
    void createDocument() {

    }

    Document getSelectedDocument() {
        return documentTable.getSelectionModel().getSelectedItem();
    }
}

class CreateDocumentView {
    GridPane grid = new GridPane();
    Label nameLabel = new Label("파일 이름");
    TextField nameField = new TextField();
    Label promptLabel = new Label("");
    Button createButton = new Button("파일 생성");


    public CreateDocumentView() {

    }
    void initLayout() {
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(createButton, 0, 1);
        grid.add(promptLabel, 1, 1);
        grid.setHgap(10);
        Stage startStage = new Stage();
        Scene scene = new Scene(grid, 300, 60);
        startStage.setScene(scene);
        startStage.setTitle("생성");
        startStage.show();
    }
    //모든 이벤트 처리
    void initListeners() {
        createButton.setOnAction(e -> {
            //접속 버튼을 입력했을 때 이벤트

        });
        nameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                //엔터를 입력했을 때 이벤트

            }
        });

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
}
