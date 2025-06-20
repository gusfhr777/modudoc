package com.piltong.modudoc.client.controller;



import com.piltong.modudoc.client.model.*;


import com.piltong.modudoc.client.network.ClientNetworkHandler;
import com.piltong.modudoc.client.view.DashboardScene;
import com.piltong.modudoc.client.view.EditorScene;
import com.piltong.modudoc.common.network.ClientCommand;
import com.piltong.modudoc.client.view.DocCreateScene;

import java.util.ArrayList;
import java.util.List;


public class DashboardController {
    // 서버에서 보낸 Document 객체를 받을 리스트 생성
    private List<Document> documentList = new ArrayList<>();

    private ClientNetworkHandler networkHandler;
    private NetworkController NetworkController;

    // 목록화면 뷰
    private DashboardScene dashboardScene;

    private DocCreateScene editDocumentView;

    private boolean isEditing = false;


    // 생성자에게 뷰를 받아서 필드에 저장
    public DashboardController(ClientNetworkHandler networkHandler, DashboardScene dashboardScene) {
        this.networkHandler = networkHandler;
        this.dashboardScene = dashboardScene;
    }
    //뷰를 입력받지 않는 생성자
    public DashboardController(ClientNetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public void start() {
        new Thread(networkHandler).start();
        dashboardScene.initialize();
        networkHandler.sendCommand(ClientCommand.READ_DOCUMENT_LIST,null);
        dashboardScene.setDocumentList(documentList);


    }

    //뷰 입력 받기
    public void setView(DashboardScene dashboardScene) {
        this.dashboardScene = dashboardScene;
    }
    public void setListener(NetworkController NetworkController) {
        this.NetworkController = NetworkController;
    }


    // 서버에서 문서 목록을 받아서 리스트에 저장 후 화면 목록에 문서 하나씩 추가
    public void loadDocumentList(List<Document> serverDocuments) {
        documentList.clear();
        documentList.addAll(serverDocuments);
        dashboardScene.setDocumentList(documentList);
    }

    // 현재 저장된 문서 리스트 반환
    public List<Document> getDocumentList(){
        return documentList;
    }

    public void addDocument(Document document) {
        documentList.add(document);
        dashboardScene.addDocument(document);
    }





    //문서 생성 창 생성
    public void createDocument() {
        if (isEditing) {
            throw new RuntimeException("Document is already editing");
        } else {
            isEditing = true;
            editDocumentView = new DocCreateScene(this);
            editDocumentView.initialize();
            editDocumentView.setButtonText("문서 생성");
            editDocumentView.showView();
        }
    }

    //문서 생성 요청
    public void sendCreateDocument(String title) {

        networkHandler.sendCommand(ClientCommand.CREATE_DOCUMENT, title);
    }

    //목록에 있는 문서 제거
    public void removeDocument(Document document) {
        networkHandler.sendCommand(ClientCommand.DELETE_DOCUMENT,document.getId());
        
    }

    //문서 수정 화면 생성
    public void editDocument(Document document) {
        if(!dashboardScene.isSelectedEmpty()) {
            if (isEditing) {
                throw new RuntimeException("Document is already editing");
            } else {
                isEditing = true;
                editDocumentView = new DocCreateScene(this);
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
            networkHandler.sendCommand(ClientCommand.UPDATE_DOCUMENT, newdocument);
            dashboardScene.removeDocument(olddocument);
            addDocument(newdocument);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void requestConnect(Document document) {
        networkHandler.sendCommand(ClientCommand.READ_DOCUMENT,document.getId());
    }
    //문서 접속
    public void connectDocument(Document document) {
        EditorScene editorScene = new EditorScene();
        EditorController editorController = new EditorController(editorScene, networkHandler, document);
        editorScene.insertStringText(document.getContent(),0);
        NetworkController.setTextEditorController(editorController);
        editorScene.showView();
    }

    public boolean getIsEditing() {
        return isEditing;
    }
    public void setIsEditing(boolean isEditing) {
        this.isEditing = isEditing;
    }

}