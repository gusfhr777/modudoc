package com.piltong.modudoc.client.controller;


import com.piltong.modudoc.client.view.EditorView;
import com.piltong.modudoc.client.network.NetworkHandler;
import com.piltong.modudoc.client.view.MainView;
import com.piltong.modudoc.common.model.*;
import com.piltong.modudoc.client.model.*;

public class EditorController implements Controller{


    // 모델
    private Document document;

    // 컨트롤러, 뷰, 서비스
    private MainController mainController;
    private MainView mainView;
    private EditorView editorView;
    private NetworkService networkService;


    //생성자, 뷰와 네트워크를 입력받아 생성하거나 네트워크만 입력받아 생성
    public EditorController(MainController mainController) {
        this.mainController = mainController;
        this.mainView = mainController.
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







    //뷰 설정
    public void setView(EditorView editorView) {
        this.editorView = editorView;
    }
    //네트워크 설정
    public void setNetworkHandler(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
    public Document getDocument() {
        return document;
    }

    public void insertText(String text, int position) {
        editorView.insertStringText(text,position);
    }
    public void deleteText(int position, int length) {
        editorView.deleteText(position, position+length);
    }

    //문서가 편집되었을 때 편집사항을 서버로 전송
    public void sendDeleteText(int from, int to,String text) {
        networkHandler.sendOperation(new Operation(OperationType.DELETE,document.getId(),to,text));
    }
    public void sendInsertText(int from, int to, String text) {
        networkHandler.sendOperation(new Operation(OperationType.INSERT,document.getId(), from,text));
    }

}
