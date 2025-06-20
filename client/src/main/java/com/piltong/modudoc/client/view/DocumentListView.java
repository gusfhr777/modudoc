package com.piltong.modudoc.client.view;

import com.piltong.modudoc.client.controller.DocumentListController;

import com.piltong.modudoc.client.model.Document;


import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DocumentListView {

    DocumentListController controller;

    TableView<Document> documentTable = new TableView<>(); //문서 목록이 표시될 도표

    //도표에 첫번째 행에 각 열의 정보 표시
    TableColumn<Document, String> titleColumn = new TableColumn<>("파일 이름");
    TableColumn<Document, String> createdDateColumn = new TableColumn<>("생성 일자");
    TableColumn<Document, String> modifiedDateColumn = new TableColumn<>("수정 일자");
    TableColumn<Document, Integer> usercountColumn = new TableColumn<>("접속 인원");

    //도표에서 문서를 선택할 때 필요한 모델
    TableView.TableViewSelectionModel<Document> selectionModel;

    //gui 구성요소를 세로로 배치하는 틀. 가장 마지막에 구성요소들 배치
    VBox vBox = new VBox();
    //각 버튼들이 들어갈 가로로 배치하는 틀
    HBox hBox = new HBox();
    //시간 정보들을 string으로 바꾸기 위한 formatter
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //파일 정보 수정에 필요한 버튼들
    Button createButton = new Button("파일 생성");
    Button InButton = new Button("파일 접속");
    Button RemoveButton = new Button("파일 삭제");
    Button ModifyButton = new Button("파일 수정");

    Label concoleLabel = new Label("");
    Stage DocumentListStage = new Stage();

    //컨트롤러 설정
    public void setController(DocumentListController controller) {
        this.controller = controller;
    }

    public void initialize() {
        initComponents();
        initLayout();
        initListeners();
    }

    //각 구성요소들을 초기화하는 메소드
    public void initComponents() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        createdDateColumn.setCellValueFactory(celldata ->{
            LocalDateTime time = celldata.getValue().getCreatedDate();
            return new SimpleStringProperty(time.format(formatter));
        });
        modifiedDateColumn.setCellValueFactory(celldata -> {
            LocalDateTime time = celldata.getValue().getModifiedDate();
            return new SimpleStringProperty(time.format(formatter));
        });

        //도표에서 원하는 문서를 선택할 때 필요한 선택 모델 설정
        selectionModel = documentTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);



    }
    //각 구성요소들을 배치하는 메소드
    public void initLayout() {
        hBox.getChildren().addAll(createButton, InButton,ModifyButton, RemoveButton);
        vBox.getChildren().addAll(hBox, documentTable, concoleLabel);
        documentTable.getColumns().addAll(titleColumn, createdDateColumn, modifiedDateColumn, usercountColumn);
        Scene scene = new Scene(vBox);
        DocumentListStage.setScene(scene);
        DocumentListStage.setTitle("파일 목록");

    }

    //이벤트들을 감지하는 메소드
    public void initListeners() {
        createButton.setOnAction(e -> {
            //생성 버튼 입력시 이벤트
            controller.createDocument();
        });
        InButton.setOnAction(e -> {
            //접속 버튼 입력시 이벤트
            if (selectionModel.getSelectedItem() != null) {
                controller.requestConnect(selectionModel.getSelectedItem());
            }
            else {
                setConcoleLabel("문서를 선택해 주세요");
            }
        });
        ModifyButton.setOnAction(e->{
            //수정 버튼 입력시 이벤트
            if(selectionModel.getSelectedItem()!=null) {
                controller.editDocument(selectionModel.getSelectedItem());
            }
            else {
                setConcoleLabel("문서를 선택해 주세요");
            }
        });
        RemoveButton.setOnAction(e -> {
            //삭제 버튼 입력시 이벤트
            if(documentTable.getSelectionModel().getSelectedItem() != null) {
                controller.removeDocument(selectionModel.getSelectedItem());
            }
            else {
                setConcoleLabel("문서를 선택해 주세요");
            }
        });
    }


    //뷰를 보이고 닫는 메소드
    public void showView() {
        DocumentListStage.show();
    }
    public void closeView() {
        DocumentListStage.close();
    }

    public void setConcoleLabel(String concoleLabel) {
        this.concoleLabel.setText(concoleLabel);
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
        for (Document document : documentList) {
            addDocument(document); // 목록 뷰 클래스 불러오기
        }
    }
    //목록의 모든 문서를 제거하는 메소드
    public void clearDocumentList() {
        documentTable.getItems().clear();
    }
    //선택된 문서를 반환하는 메소드
    public Document getSelectedDocument() {
        return selectionModel.getSelectedItem();
    }
    public boolean isSelectedEmpty() {
        return selectionModel.getSelectedItem() == null;
    }

}
