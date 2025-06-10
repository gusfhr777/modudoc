package com.piltong.modudoc.client.controller;

import com.piltong.modudoc.client.network.ClientNetworkHandler;
import com.piltong.modudoc.client.view.DocumentListView;
import com.piltong.modudoc.common.document.Document;
import com.piltong.modudoc.common.network.ClientCommand;
import com.piltong.modudoc.common.network.ClientNetworkListener;
import com.piltong.modudoc.common.operation.Operation;
import com.piltong.modudoc.client.view.EditDocumentView;

import java.util.ArrayList;
import java.util.List;


public class DocumentListController {
    // 서버에서 보낸 Document 객체를 받을 리스트 생성
    List<Document> documentList = new ArrayList<>();

    ClientNetworkHandler networkHandler;

    EditDocumentView editDocumentView;

    // 목록화면
    private DocumentListView documentListView;

    boolean isCreating = false;

    //뷰 입력 받기
    void setView(DocumentListView documentListView) {
        this.documentListView = documentListView;
        this.documentListView.initialize();
    }

    // 생성자에게 뷰를 받아서 필드에 저장
    public DocumentListController(ClientNetworkHandler networkHandler,DocumentListView documentListView) {
        this.networkHandler = networkHandler;
        this.documentListView = documentListView;
        this.documentListView.initialize();
    }
    public DocumentListController(ClientNetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    // 서버에서 문서 목록을 받아서 리스트에 저장 후 화면 목록에 문서 하나씩 추가
    public void loadDocumentList(List<Document> serverDocuments) {
        documentList.clear();
        documentList.addAll(serverDocuments);
        documentListView.setDocumentList(documentList);
    }

    // 현재 저장된 문서 리스트 반환
    public List<Document> getDocumentList(){
        return documentList;
    }



    //문서 생성
    public void createDocument() {
        if (isCreating) {

        }
        else {
            editDocumentView = new EditDocumentView(this);
            editDocumentView.initialize();

        }

    }

    public void sendCreateDocument(String title) {

    }

    //목록에 있는 문서 제거
    public void removeDoucment(Document document) {
        documentListView.removeDocument(document);
        documentList.remove(document);
    }

    class DocumentListNetwork implements ClientNetworkListener {
        @Override
        public void onNetworkError(Throwable t) {

        }

        @Override
        public <T> void onCommandSuccess(ClientCommand command, T payload) {
            //문서 리스트를 입력받았을 때
            if(command == ClientCommand.READ_DOCUMENT_SUMMARIES) {
                documentListView.setDocumentList((List<Document>) payload);
            }
        }

        @Override
        public void onCommandFailure(ClientCommand command, String errorMessage) {

        }

        @Override
        public void onOperationReceived(Operation op) {

        }

    }

}
