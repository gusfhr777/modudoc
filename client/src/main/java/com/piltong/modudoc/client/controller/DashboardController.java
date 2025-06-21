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

    private static final Logger log = LogManager.getLogger(DashboardController.class);
    // 서버에서 보낸 Document 객체를 받을 리스트 생성
    private List<Document> documentList = new ArrayList<>();

    // 컨트롤러
    private final MainController mainController;
    private final NetworkHandler networkHandler;


    // 뷰
    private final DashboardView dashboardView;


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

    public Parent getView() {
        return this.dashboardView.getRoot();
    }

    public void initListeners() {
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
    }









    // 내부 호출. 문서 생성 창을 연다.
    public void openCreateDocumentDialog() {
        log.info("openCreateDocumentDialog()");
        createDocumentDialog = new CreateDocumentDialog();
        createDocumentDialog.getNameLabel().setText("문서 제목 :");
        createDocumentDialog.getEditButton().setText("문서 생성");

        this.dialogStage = new Stage();
        dialogStage.setScene(new Scene(createDocumentDialog.getGrid(), 300, 200));

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
    public void createDocument() {
        log.info("createDocument()");
        String title = this.createDocumentDialog.getTitleField().getText();
        if (title.isEmpty()) {
            this.createDocumentDialog.setPrompt("문서 제목을 입력하세요.");
            return;
        }

        networkHandler.sendCommand(ClientCommand.CREATE_DOCUMENT, title);

        this.dialogStage.close(); // 다일로그 종료
        
    }




    public void openEditDocumentDialog(Document document) {
        log.info("openEditDocumentDialog()");

        createDocumentDialog = new CreateDocumentDialog();
        createDocumentDialog.getTitleField().setText(document.getTitle());
        createDocumentDialog.getNameLabel().setText("문서 제목 :");
        createDocumentDialog.getEditButton().setText("문서 수정");

        this.dialogStage = new Stage();
        dialogStage.setScene(new Scene(createDocumentDialog.getGrid(), 300, 200));



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
    public void editDocument(Document document) {
        log.info("editDocument()");
        String title = this.createDocumentDialog.getTitleField().getText();
        if (title.isEmpty()) {
            this.createDocumentDialog.setPrompt("문서 제목을 입력하세요.");
            return;
        }
        document.setTitle(title);
        document.setContent("");
        networkHandler.sendCommand(ClientCommand.UPDATE_DOCUMENT, document);
        deleteDocument(document.getId());

        this.dialogStage.close();
    }

    // 내부 호출. 문서에 접속한다. -> EditorView Open, READ_DOCUMENT
    public void requestConnect(Document document) {
        log.info("requestConnect()");

        networkHandler.sendCommand(ClientCommand.READ_DOCUMENT, document);
    }

    // 내부 호출. 문서를 삭제한다. -> REMOVE_DOCUMENT
    public void removeDocument(Document document) {
        log.info("removeDocument()");
        networkHandler.sendCommand(ClientCommand.DELETE_DOCUMENT, document.getId());
        deleteDocument(document.getId());
    }


    // NetworkListenerImpl에서 호출. documentList를 할당받고, View에 반영한다.
    public void loadDocumentList(List<Document> documentList) {
        log.info("loadDocumentList()");
        this.documentList.clear();
        this.documentList.addAll(documentList);

        for (Document document: this.documentList) {
            this.dashboardView.getDocumentTable().getItems().add(document);
        }
    }

    // NetworkListenerImpl 호출. documentList에 document를 추가한다.
    public void addDocument(Document document) {
        log.info("addDocument()");
        this.documentList.add(document);
        this.dashboardView.getDocumentTable().getItems().add(document);
    }

    public void deleteDocument(Integer id) {
        log.info("DeleteDocument()");
        for(Document doc: this.documentList) {
            if(doc.getId().equals(id)) {
                this.documentList.remove(doc);
                this.dashboardView.getDocumentTable().getItems().remove(doc);
                return;
            }
        }
    }


//    private NetworkListenerImpl networkController;
//    private MainView mainView;
//    private DocCreateView docCreateView;
//
//
//    // 생성자
//
//
//
//    public void setView(View view) {
//        this.mainView = (MainView) view;
//        this.dashboardView = this.mainView.getDashboardView();
//
//    }
//
//    // 초기화
//    public void init() {
//        this.dashboardView.initComponents();
//        this.dashboardView.initLayout();
//        this.dashboardView.initListeners(this);
//    }
//
//    // 시작
//    public void start() {
//        dashboardView.start();
//    }
//
//    // 끝
//    public void end() {
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
//    // dashboard 시작
////    public void start() {
////        dashboardView.initialize();
////        documentList = networkController.sendCommand(ClientCommand.READ_DOCUMENT_LIST, null);
////        dashboardView.setDocumentList(documentList);
////    }
//
//
//    // 서버에서 문서 목록을 받아서 리스트에 저장 후 화면 목록에 문서 하나씩 추가
//    public void loadDocumentList(List<Document> newDocumnetList) {
//        documentList.clear();
//        documentList.addAll(newDocumnetList);
//        dashboardView.setDocumentList(documentList);
//    }
//
//    // 현재 저장된 문서 리스트 반환
//    public List<Document> getDocumentList(){
//        return documentList;
//    }
//
//    public void addDocument(Document document) {
//        documentList.add(document);
//        dashboardView.addDocument(document);
//    }
//
//
//
//
//
//    //문서 생성 창 생성
//    public void createDocument() {
//        if (isEditing) {
//            throw new RuntimeException("Document is already editing");
//        } else {
//            isEditing = true;
//            docCreateView = new DocCreateView();
//            docCreateView.initialize(this);
//            docCreateView.setButtonText("문서 생성");
//            docCreateView.start();
//        }
//    }
//
//    //문서 생성 요청
//    public void sendCreateDocument(String title) {
//
//        networkHandler.sendCommand(ClientCommand.CREATE_DOCUMENT, title);
//    }
//
//    //목록에 있는 문서 제거
//    public void removeDocument(Document document) {
//        networkHandler.sendCommand(ClientCommand.DELETE_DOCUMENT,document.getId());
//
//    }
//
//    //문서 수정 화면 생성
//    public void editDocument(Document document) {
//        if(!dashboardView.isSelectedEmpty()) {
//            if (isEditing) {
//                throw new RuntimeException("Document is already editing");
//            } else {
//                isEditing = true;
//                docCreateView = new DocCreateView();
//                docCreateView.initialize(this);
//                docCreateView.setDocument(document);
//                docCreateView.setButtonText("문서 제목 수정");
//                docCreateView.showView();
//            }
//        }
//        else throw new RuntimeException("Document is not selected");
//    }
//
//    //문서 수정 요청
//    public void sendEditDocument(Document olddocument, Document newdocument) {
//        try {
//            networkHandler.sendCommand(ClientCommand.UPDATE_DOCUMENT, newdocument);
//            dashboardView.removeDocument(olddocument);
//            addDocument(newdocument);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//    public void requestConnect(Document document) {
//        networkHandler.sendCommand(ClientCommand.READ_DOCUMENT,document.getId());
//    }
//    //문서 접속
//    public void connectDocument(Document document) {
////        EditorView editorScene = new EditorView();
////        EditorController editorController = new EditorController(editorScene, networkController, document);
//
//        mainController.startEditor(document);
//        mainView.getEditorView().insertStringText(document.getContent(),0);
////        NetworkListenerImpl.setTextEditorController(editorController);
//        mainView.getEditorView().start();
//    }
//
//    public boolean getIsEditing() {
//        return isEditing;
//    }
//    public void setIsEditing(boolean isEditing) {
//        this.isEditing = isEditing;
//    }

}