package com.piltong.modudoc.client.controller;



import com.piltong.modudoc.client.model.*;


import com.piltong.modudoc.client.network.NetworkHandler;
import com.piltong.modudoc.client.view.*;
import com.piltong.modudoc.common.network.ClientCommand;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class DashboardController{

    // 로거
    private static final Logger log = LogManager.getLogger(DashboardController.class);

    // 서버에서 보낸 Document 객체를 받을 리스트
    private List<Document> documentList = new ArrayList<>();

    // 컨트롤러
    private final MainController mainController;
    private final NetworkHandler networkHandler;

    // 뷰
    private final DashboardView dashboardView;

    // 다일로그
    private CreateDocumentDialog createDocumentDialog;
    private Stage dialogStage;



    // 생성자
    public DashboardController(MainController mainController, NetworkHandler networkHandler) {
        this.mainController = mainController;
        this.networkHandler = networkHandler;
        this.dashboardView = new DashboardView();
        initListeners();
        log.info("DashboardController initialize.");

    }

    // 뷰 리턴
    public Parent getView() {
        return this.dashboardView.getRoot();
    }

    // NetworkListenerImpl에서 호출. documentList를 할당받고, View에 반영한다.
    public void loadDashboard(List<Document> documentList) {
        log.info("loadDashboard()");

        this.documentList.clear();
        this.documentList.addAll(documentList);
        this.dashboardView.getDocumentTable().getItems().clear();

        for (Document document : this.documentList) {
            this.dashboardView.getDocumentTable().getItems().add(document);
        }
    }

    // networkService 호출. documentList에 document를 추가한다.
    public void addDocument(Document document) {
        log.info("addDocument()");
        this.documentList.add(document);
        this.dashboardView.getDocumentTable().getItems().add(document);
    }

    public void deleteDocument(Integer id) {
        log.info("DeleteDocument()");
        for (Document doc : this.documentList) {
            if (doc.getId().equals(id)) {
                this.documentList.remove(doc);
                this.dashboardView.getDocumentTable().getItems().remove(doc);
                return;
            }
        }
    }

    public void changeDocument(Document document) {
        log.info("editDocument()");
        for(Document doc: dashboardView.getDocumentTable().getItems()) {
            if(doc.getId().equals(document.getId())) {
                this.documentList.remove(doc);
                this.documentList.add(document);
                doc.setTitle(document.getTitle());
                doc.setModifiedDate(document.getModifiedDate());
                dashboardView.getDocumentTable().refresh();
            }
        }

    }





    // 리스너 초기화
    private void initListeners() {
        this.dashboardView.getCreateButton().setOnAction(e -> {
            //생성 버튼 입력시 이벤트
            openCreateDocumentDialog();
        });
        this.dashboardView.getInButton().setOnAction(e -> {
            //접속 버튼 입력시 이벤트
            if (this.dashboardView.getSelectionModel().getSelectedItem() != null) {
                requestConnect(this.dashboardView.getSelectionModel().getSelectedItem());
            }
            else {
                this.dashboardView.setConsoleText("문서를 선택해 주세요");
            }
        });
        this.dashboardView.getModifyButton().setOnAction(e->{
            //수정 버튼 입력시 이벤트
            if(this.dashboardView.getSelectionModel().getSelectedItem()!=null) {
                openEditDocumentDialog(this.dashboardView.getSelectionModel().getSelectedItem());
                //editDocument(this.dashboardView.getSelectionModel().getSelectedItem());
            }
            else {
                this.dashboardView.setConsoleText("문서를 선택해 주세요");
            }
        });
        this.dashboardView.getRemoveButton().setOnAction(e -> {
            //삭제 버튼 입력시 이벤트
            if(this.dashboardView.getSelectionModel().getSelectedItem() != null) {
                removeDocument(this.dashboardView.getSelectionModel().getSelectedItem());
            }
            else {
                this.dashboardView.setConsoleText("문서를 선택해 주세요");
            }
        });
        this.dashboardView.getRefreshButton().setOnAction(e -> {
           networkHandler.sendCommand(ClientCommand.READ_DOCUMENT_LIST,null);
        });
    }



    // 내부 호출. 문서 생성 창을 연다.
    private void openCreateDocumentDialog() {
        log.info("openCreateDocumentDialog()");
        createDocumentDialog = new CreateDocumentDialog();
        createDocumentDialog.getNameLabel().setText("문서 제목 :");
        createDocumentDialog.getEditButton().setText("문서 생성");

        this.dialogStage = new Stage();
        dialogStage.setResizable(false);
        dialogStage.setScene(new Scene(createDocumentDialog.getGrid(), 300, 60));

        createDocumentDialog.getEditButton().setOnAction(e -> {
            //생성 버튼을 입력했을 때 이벤트
            createDocument();
        });
        createDocumentDialog.getTitleField().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                //엔터를 입력했을 때 이벤트
                createDocument() ;
            }
        });

        this.dialogStage.show();
//        stage.setOnCloseRequest(e->{
//            controller.setIsEditing(false);
//        });
    }


    // 내부 호출. 문서를 생성한다. CREATE_DOCUMENT.
    private void createDocument() {
        log.info("createDocument()");
        String title = this.createDocumentDialog.getTitleField().getText();
        if (title.isEmpty()) {
            this.createDocumentDialog.setPrompt("문서 제목을 입력하세요.");
            return;
        }

        networkHandler.sendCommand(ClientCommand.CREATE_DOCUMENT, title);

        this.dialogStage.close(); // 다일로그 종료
        
    }




    private void openEditDocumentDialog(Document document) {
        log.info("openEditDocumentDialog()");

        createDocumentDialog = new CreateDocumentDialog();
        createDocumentDialog.getTitleField().setText(document.getTitle());
        createDocumentDialog.getNameLabel().setText("문서 제목 :");
        createDocumentDialog.getEditButton().setText("문서 수정");

        this.dialogStage = new Stage();
        dialogStage.setResizable(false);
        dialogStage.setScene(new Scene(createDocumentDialog.getGrid(), 300, 60));



        createDocumentDialog.getEditButton().setOnAction(e -> {
            //생성 버튼을 입력했을 때 이벤트
            editDocument(document);
        });
        createDocumentDialog.getTitleField().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                //엔터를 입력했을 때 이벤트
                editDocument(document);
            }
        });

        this.dialogStage.show();
    }





    // 내부 호출. 문서를 수정한다. -> UPDATE_DOCUMENT
    private void editDocument(Document document) {
        log.info("editDocument()");
        String title = this.createDocumentDialog.getTitleField().getText();
        if (title.isEmpty()) {
            this.createDocumentDialog.setPrompt("문서 제목을 입력하세요.");
            return;
        }
        document.setTitle(title);
        document.setContent("");
        networkHandler.sendCommand(ClientCommand.UPDATE_DOCUMENT, document);
        changeDocument(document);

        this.dialogStage.close();
    }

    // 내부 호출. 문서에 접속한다. -> EditorView Open, READ_DOCUMENT
    private void requestConnect(Document document) {
        log.info("requestConnect()");

        networkHandler.sendCommand(ClientCommand.READ_DOCUMENT, document.getId());
    }

    // 내부 호출. 문서를 삭제한다. -> REMOVE_DOCUMENT
    private void removeDocument(Document document) {
        log.info("removeDocument()");
        networkHandler.sendCommand(ClientCommand.DELETE_DOCUMENT, document.getId());
        deleteDocument(document.getId());
    }

}