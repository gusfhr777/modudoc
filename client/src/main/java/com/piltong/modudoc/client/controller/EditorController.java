package com.piltong.modudoc.client.controller;


import com.piltong.modudoc.client.view.EditorView;
import com.piltong.modudoc.client.network.NetworkHandler;
import com.piltong.modudoc.client.model.*;
import javafx.scene.Parent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EditorController {

    // 모델
    private Document document;

    private static final Logger log = LogManager.getLogger(EditorController.class);

    private final MainController mainController;
    private final NetworkHandler networkHandler;
    private final EditorView editorView;


    public EditorController(MainController mainController, NetworkHandler networkHandler) {
        this.mainController = mainController;
        this.networkHandler = networkHandler;

        this.editorView = new EditorView();
    }

    public Parent getView() {
        return this.editorView.getRoot();
    }

    public void setDocument(Document document) {
        this.document = document;
    }


//
//    // 컨트롤러, 뷰
//    private MainController mainController;
//    private MainView mainView;
//    private EditorView editorView;
//    private NetworkHandler networkHandler;
//
//
//    //생성자, 뷰와 네트워크를 입력받아 생성하거나 네트워크만 입력받아 생성
//    public EditorController(MainController mainController) {
//        this.mainController = mainController;
//    }
//
//
//    public void setView(View view) {
//        this.mainView = (MainView) view;
//        this.editorView = this.mainView.getEditorView();
//    }
//
//    // 초기화
//    public void init(Document document) {
//        this.document = document;
//        this.editorView.initialize(document, this);
//    }
//
//    // 시작
//    public void start() {
//
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
//    //네트워크 설정
//    public void setNetworkHandler(NetworkHandler networkHandler) {
//        this.networkHandler = networkHandler;
//    }
//
//    public void setDocument(Document document) {
//        this.document = document;
//    }
//    public Document getDocument() {
//        return document;
//    }
//
//    public void insertText(String text, int position) {
//        editorView.insertStringText(text,position);
//    }
//    public void deleteText(int position, int length) {
//        editorView.deleteText(position, position+length);
//    }
//
//    //문서가 편집되었을 때 편집사항을 서버로 전송
//    public void sendDeleteText(int from, int to,String text) {
//        networkHandler.sendOperation(new Operation(OperationType.DELETE,document.getId(),to,text));
//    }
//    public void sendInsertText(int from, int to, String text) {
//        networkHandler.sendOperation(new Operation(OperationType.INSERT,document.getId(), from,text));
//    }

}
