package com.piltong.modudoc.client.view;

import com.piltong.modudoc.client.model.Document;


import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardView {


    TableView<Document> documentTable = new TableView<>(); //문서 목록이 표시될 도표
//
//    //도표에 첫번째 행에 각 열의 정보 표시
    TableColumn<Document, String> titleColumn = new TableColumn<>("파일 이름");
    TableColumn<Document, String> createdDateColumn = new TableColumn<>("생성 일자");
    TableColumn<Document, String> modifiedDateColumn = new TableColumn<>("수정 일자");

    public TableView.TableViewSelectionModel<Document> getSelectionModel() {
        return selectionModel;
    }


    //도표에서 문서를 선택할 때 필요한 모델
    TableView.TableViewSelectionModel<Document> selectionModel;

    //gui 구성요소를 세로로 배치하는 틀. 가장 마지막에 구성요소들 배치
    VBox root = new VBox();
    //각 버튼들이 들어갈 가로로 배치하는 틀
    HBox hBox = new HBox();
    HBox refreshHbox = new HBox();
    //시간 정보들을 string으로 바꾸기 위한 formatter
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //파일 정보 수정에 필요한 버튼들
    Button createButton = new Button("문서 생성");
    Button InButton = new Button("문서 접속");
    Button RemoveButton = new Button("문서 삭제");
    Button ModifyButton = new Button("문서 수정");
    Button refreshButton = new Button("🔄");

    public void setConsoleText(String text) {
        this.consoleLabel.setText(text);
    }

    Label consoleLabel = new Label("");


    // Getter
    public TableView<Document> getDocumentTable() {
        return documentTable;
    }

    public Button getCreateButton() {
        return createButton;
    }

    public Button getInButton() {
        return InButton;
    }

    public Button getRemoveButton() {
        return RemoveButton;
    }

    public Button getModifyButton() {
        return ModifyButton;
    }

    public Button getRefreshButton() {return refreshButton;}

    public Parent getRoot() {
        return root;
    }



    public DashboardView() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        createdDateColumn.setCellValueFactory(celldata ->{
            return new SimpleStringProperty(celldata.getValue().getCreatedDate().format(formatter));
        });
        modifiedDateColumn.setCellValueFactory(celldata -> {
            return new SimpleStringProperty(celldata.getValue().getModifiedDate().format(formatter));
        });


        //도표에서 원하는 문서를 선택할 때 필요한 선택 모델 설정
        selectionModel = documentTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        documentTable.setEditable(false);

        styleButton(createButton);
        styleButton(InButton);
        styleButton(ModifyButton);
        styleButton(RemoveButton);
        styleButton(refreshButton);

        createButton.setMinWidth(80);
        InButton.setMinWidth(80);
        ModifyButton.setMinWidth(80);
        RemoveButton.setMinWidth(80);
        refreshButton.setMinWidth(43);

        root.setSpacing(10);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #f5f5f5;");

        refreshHbox.setAlignment(Pos.CENTER_RIGHT);
        refreshHbox.getChildren().add(refreshButton);
        hBox.setSpacing(5);
        hBox.getChildren().addAll(createButton, InButton,ModifyButton, RemoveButton,refreshHbox);
        root.getChildren().addAll(hBox, documentTable, consoleLabel);
        documentTable.getColumns().addAll(titleColumn, createdDateColumn, modifiedDateColumn);
//        Scene scene = new Scene(vBox);
//        DocumentListStage.setScene(scene);
//        DocumentListStage.setTitle("파일 목록");
    }

    private void styleButton(Button button) {
        button.setStyle(
                "-fx-background-color: #20C997;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 10px;" +
                "-fx-background-radius: 10px;" +
                "-fx-padding: 8 16 8 16;"
        );
    }





//
//
//
//    //각 구성요소들을 초기화하는 메소드
//    public void initComponents() {
//
//
//
//    }
//    //각 구성요소들을 배치하는 메소드
//    public void initLayout() {
//
//    }
//
//    //이벤트들을 감지하는 메소드
//    public void initListeners(DashboardController controller) {
//    }
//
//
//    //뷰를 보이고 닫는 메소드
//    public void showView() {
//        DocumentListStage.show();
//    }
//    public void closeView() {
//        DocumentListStage.close();
//    }
//
//    public void setConcoleLabel(String concoleLabel) {
//        this.concoleLabel.setText(concoleLabel);
//    }
//
//
//    //목록에 문서를 추가하는 메소드
//    public void addDocument(Document document) {
//        documentTable.getItems().add(document);
//    }
//    //목록에 문서를 삭제하는 메소드
//    public void removeDocument(Document document) {
//        documentTable.getItems().remove(document);
//    }
//    //목록을 설정하는 메소드
//    public void setDocumentList(List<Document> documentList) {
//        for (Document document : documentList) {
//            addDocument(document); // 목록 뷰 클래스 불러오기
//        }
//    }
//    //목록의 모든 문서를 제거하는 메소드
//    public void clearDocumentList() {
//        documentTable.getItems().clear();
//    }
//    //선택된 문서를 반환하는 메소드
//    public Document getSelectedDocument() {
//        return selectionModel.getSelectedItem();
//    }
//    public boolean isSelectedEmpty() {
//        return selectionModel.getSelectedItem() == null;
//    }

}
