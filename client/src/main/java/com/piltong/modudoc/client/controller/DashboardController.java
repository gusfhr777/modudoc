package com.piltong.modudoc.client.controller;



import com.piltong.modudoc.client.model.*;


import com.piltong.modudoc.client.view.DashboardView;
import com.piltong.modudoc.client.view.EditorView;
import com.piltong.modudoc.client.view.MainView;
import com.piltong.modudoc.common.network.ClientCommand;
import com.piltong.modudoc.client.view.DocCreateView;

import java.util.ArrayList;
import java.util.List;


public class DashboardController implements Controller{

    // 서버에서 보낸 Document 객체를 받을 리스트 생성
    private List<Document> documentList = new ArrayList<>();
    private boolean isEditing = false;


    // 컨트롤러, 뷰
    private MainController mainController;
    private NetworkService networkService;
    private MainView mainView;
    private DashboardView dashboardView;


    // 생성자
    public DashboardController(MainController mainController) {
        this.mainController = mainController;
        this.networkService = this.mainController.getNetworkService();
    }


    // 시작
    public void start() {

    }

    // 끝
    public void end() {

    }

    // 종료
    public void shutdown() {

    }








    // dashboard 시작
//    public void start() {
//        dashboardView.initialize();
//        documentList = networkService.sendCommand(ClientCommand.READ_DOCUMENT_LIST, null);
//        dashboardView.setDocumentList(documentList);
//    }


    // 서버에서 문서 목록을 받아서 리스트에 저장 후 화면 목록에 문서 하나씩 추가
    public void loadDocumentList(List<Document> newDocumnetList) {
        documentList.clear();
        documentList.addAll(newDocumnetList);
        dashboardView.setDocumentList(documentList);
    }

    // 현재 저장된 문서 리스트 반환
    public List<Document> getDocumentList(){
        return documentList;
    }

    public void addDocument(Document document) {
        documentList.add(document);
        dashboardView.addDocument(document);
    }





    //문서 생성 창 생성
    public void createDocument() {
        if (isEditing) {
            throw new RuntimeException("Document is already editing");
        } else {
            isEditing = true;
            editDocumentView = new DocCreateView(this);
            editDocumentView.initialize();
            editDocumentView.setButtonText("문서 생성");
            editDocumentView.showView();
        }
    }

    //문서 생성 요청
    public void sendCreateDocument(String title) {

        networkService.sendCommand(ClientCommand.CREATE_DOCUMENT, title);
    }

    //목록에 있는 문서 제거
    public void removeDocument(Document document) {
        networkService.sendCommand(ClientCommand.DELETE_DOCUMENT,document.getId());
        
    }

    //문서 수정 화면 생성
    public void editDocument(Document document) {
        if(!dashboardView.isSelectedEmpty()) {
            if (isEditing) {
                throw new RuntimeException("Document is already editing");
            } else {
                isEditing = true;
                editDocumentView = new DocCreateView(this);
                editDocumentView.initialize();
                editDocumentView.setDocument(document);
                editDocumentView.setButtonText("문서 제목 수정");
                editDocumentView.showView();
            }
        }
        else throw new RuntimeException("Document is not selected");
    }

    //문서 수정 요청
    public void sendEditDocument(Document olddocument, Document newdocument) {
        try {
            networkService.sendCommand(ClientCommand.UPDATE_DOCUMENT, newdocument);
            dashboardView.removeDocument(olddocument);
            addDocument(newdocument);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void requestConnect(Document document) {
        networkService.sendCommand(ClientCommand.READ_DOCUMENT,document.getId());
    }
    //문서 접속
    public void connectDocument(Document document) {
        EditorView editorScene = new EditorView();
        EditorController editorController = new EditorController(editorScene, networkService, document);
        editorScene.insertStringText(document.getContent(),0);
        NetworkService.setTextEditorController(editorController);
        editorScene.showView();
    }

    public boolean getIsEditing() {
        return isEditing;
    }
    public void setIsEditing(boolean isEditing) {
        this.isEditing = isEditing;
    }

}