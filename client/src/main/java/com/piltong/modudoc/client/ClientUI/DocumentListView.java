package com.piltong.modudoc.client.ClientUI;

import com.piltong.modudoc.common.Document;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.time.LocalDateTime;

public class DocumentListView {
    TableView<Document> documentTable = new TableView<>();
    TableColumn<Document, String> titleColumn = new TableColumn<>("파일 이름");
    TableColumn<Document, LocalDateTime> createdDateColumn = new TableColumn<>("생성 일자");
    TableColumn<Document, LocalDateTime> modifiedDateColumn = new TableColumn<>("수정 일자");
    TableColumn<Document, Integer> usercountColumn = new TableColumn<>("접속 인원");
    DocumentListView() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        createdDateColumn.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        modifiedDateColumn.setCellValueFactory(new PropertyValueFactory<>("modifiedDate"));
        usercountColumn.setCellValueFactory(new PropertyValueFactory<>("userCount"));
    }
    void addDocument(Document document) {
        documentTable.getItems().add(document);
    }
    void createDocument() {

    }
}

class CreateDocumentView {
    GridPane grid = new GridPane();
    Label nameLabel = new Label("파일 이름");
    TextField nameField = new TextField();
    Label promptLabel = new Label("");
    Button createButton = new Button("파일 생성");
}
