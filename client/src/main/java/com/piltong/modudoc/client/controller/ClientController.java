package com.piltong.modudoc.client.controller;

import com.piltong.modudoc.client.view.DocumentListView;
import com.piltong.modudoc.client.view.TextEditorView;
import com.piltong.modudoc.common.document.Document;

import java.util.ArrayList;
import java.util.List;


public class ClientController{
    // 서버에서 보낸 Document 객체를 받을 리스트 생성
    List<Document> documentList = new ArrayList<>();

    // 목록화면
    private DocumentListView documentListView;

    // 생성자에게 뷰를 받아서 필드에 저장
    public ClientController(DocumentListView documentListView) {
        this.documentListView = documentListView;
    }

    // 서버에서 문서 목록을 받아서 리스트에 저장 후 화면 목록에 문서 하나씩 추가
    public void loadDocumentList(){List<Document>, serverDocumnets} {
        documentList.clear();
        documentList.addAll(serverDocumnets);

    }

    // 현재 저장된 문서 리스트 반환
    public List<Document> getDocumentList(){
        return documentList;
    }

}
