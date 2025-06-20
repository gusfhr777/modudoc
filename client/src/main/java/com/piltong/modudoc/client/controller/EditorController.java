package com.piltong.modudoc.client.controller;


import com.piltong.modudoc.client.view.EditorScene;
import com.piltong.modudoc.client.network.ClientNetworkHandler;
import com.piltong.modudoc.common.model.*;
import com.piltong.modudoc.client.model.*;

public class EditorController {
    private EditorScene editorScene;
    private ClientNetworkHandler networkHandler;
    private Document document;

    //생성자, 뷰와 네트워크를 입력받아 생성하거나 네트워크만 입력받아 생성
    public EditorController(EditorScene editorScene, ClientNetworkHandler networkHandler, Document document) {
        this.editorScene = editorScene;
        this.networkHandler = networkHandler;
        this.document = document;
    }
    public EditorController(ClientNetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    //뷰 설정
    public void setView(EditorScene editorScene) {
        this.editorScene = editorScene;
    }
    //네트워크 설정
    public void setNetworkHandler(ClientNetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
    public Document getDocument() {
        return document;
    }

    public void insertText(String text, int position) {
        editorScene.insertStringText(text,position);
    }
    public void deleteText(int position, int length) {
        editorScene.deleteText(position, position+length);
    }

    //문서가 편집되었을 때 편집사항을 서버로 전송
    public void sendDeleteText(int from, int to,String text) {
        networkHandler.sendOperation(new Operation(OperationType.DELETE,document.getId(),to,text));
    }
    public void sendInsertText(int from, int to, String text) {
        networkHandler.sendOperation(new Operation(OperationType.INSERT,document.getId(), from,text));
    }

}
