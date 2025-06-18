package com.piltong.modudoc.client.controller;

import com.piltong.modudoc.client.network.ClientNetworkHandler;
import com.piltong.modudoc.client.view.DocumentListView;
import com.piltong.modudoc.client.view.TextEditorView;
import com.piltong.modudoc.common.document.Document;
import com.piltong.modudoc.common.document.DocumentSummary;
import com.piltong.modudoc.common.network.ClientCommand;
import com.piltong.modudoc.common.network.ClientNetworkListener;
import com.piltong.modudoc.common.operation.Operation;
import com.piltong.modudoc.client.view.EditDocumentView;
import com.piltong.modudoc.client.network.NetworkListener;
import com.piltong.modudoc.common.operation.OperationType;

import java.util.ArrayList;
import java.util.List;


public class DocumentListController {
    // 서버에서 보낸 Document 객체를 받을 리스트 생성
    List<DocumentSummary> documentList = new ArrayList<>();

    ClientNetworkHandler networkHandler;

    EditDocumentView editDocumentView;

    NetworkListener NetworkListener;

    // 목록화면
    private DocumentListView documentListView;

    boolean isEditing = false;


    // 생성자에게 뷰를 받아서 필드에 저장
    public DocumentListController(ClientNetworkHandler networkHandler,DocumentListView documentListView) {
        this.networkHandler = networkHandler;
        this.documentListView = documentListView;
    }
    //뷰를 입력받지 않는 생성자
    public DocumentListController(ClientNetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public void start() {
        new Thread(networkHandler).start();
        documentListView.initialize();
        networkHandler.sendCommand(ClientCommand.READ_DOCUMENT_SUMMARIES,null);
        documentListView.setDocumentList(documentList);


    }

    //뷰 입력 받기
    void setView(DocumentListView documentListView) {
        this.documentListView = documentListView;
    }
    void setListener(NetworkListener NetworkListener) {
        this.NetworkListener = NetworkListener;
    }


    // 서버에서 문서 목록을 받아서 리스트에 저장 후 화면 목록에 문서 하나씩 추가
    public void loadDocumentList(List<DocumentSummary> serverDocuments) {
        documentList.clear();
        documentList.addAll(serverDocuments);
        documentListView.setDocumentList(documentList);
    }

    // 현재 저장된 문서 리스트 반환
    public List<DocumentSummary> getDocumentList(){
        return documentList;
    }

    public void addDocument(DocumentSummary document) {
        documentList.add(document);
    }





    //생성 버튼 입력 시 문서 생성
    public void createDocument() {
        if (isEditing) {
            throw new RuntimeException("Document is already editing");
        } else {
            isEditing = true;
            editDocumentView = new EditDocumentView(this);
            editDocumentView.initialize();
            editDocumentView.setButtonText("문서 생성");
            editDocumentView.showView();
        }
    }
    public void sendCreateDocument(String title) {

        networkHandler.sendCommand(ClientCommand.CREATE_DOCUMENT,new DocumentSummary(null,title,null,null,null));
    }

    //목록에 있는 문서 제거
    public void removeDocument(DocumentSummary document) {
        networkHandler.sendCommand(ClientCommand.DELETE_DOCUMENT,document);
        documentListView.removeDocument(document);
        documentList.remove(document);
    }

    //문서 수정 화면
    public void editDocument(DocumentSummary document) {
        if(!documentListView.isSelectedEmpty()) {
            if (isEditing) {
                throw new RuntimeException("Document is already editing");
            } else {
                isEditing = true;
                editDocumentView = new EditDocumentView(this);
                editDocumentView.initialize();
                editDocumentView.setButtonText("문서 제목 수정");
                editDocumentView.showView();
            }
        }
        else throw new RuntimeException("Document is not selected");
    }

    public void sendEditDocument(DocumentSummary document) {
        networkHandler.sendCommand(ClientCommand.UPDATE_DOCUMENT,document);
    }
    public void requestConnect(DocumentSummary document) {
        networkHandler.sendCommand(ClientCommand.READ_DOCUMENT,document.getId());
    }
    //문서 접속
    public void connectDocument(Document document) {
        TextEditorView textEditorView = new TextEditorView();
        TextEditorController textEditorController = new TextEditorController(textEditorView, networkHandler, networkHandler.requestDocument());
        textEditorView.showView();
    }

    public boolean getIsEditing() {
        return isEditing;
    }
    public void setIsEditing(boolean isEditing) {
        this.isEditing = isEditing;
    }

}