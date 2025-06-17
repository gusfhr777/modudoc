package com.piltong.modudoc.client.controller;


import com.piltong.modudoc.client.view.TextEditorView;
import com.piltong.modudoc.client.network.ClientNetworkHandler;
import com.piltong.modudoc.common.network.ClientCommand;
import com.piltong.modudoc.common.network.ClientNetworkListener;
import com.piltong.modudoc.common.operation.EditPosition;
import com.piltong.modudoc.common.operation.Operation;
import com.piltong.modudoc.common.operation.OperationType;

public class TextEditorController {
    private TextEditorView textEditorView;
    private ClientNetworkHandler networkHandler;

    //생성자, 뷰와 네트워크를 입력받아 생성하거나 네트워크만 입력받아 생성
    public TextEditorController(TextEditorView textEditorView, ClientNetworkHandler networkHandler) {
        this.textEditorView = textEditorView;
        this.networkHandler = networkHandler;
    }
    public TextEditorController(ClientNetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    //뷰 설정
    public void setView(TextEditorView textEditorView) {
        this.textEditorView = textEditorView;
    }
    //네트워크 설정
    public void setNetworkHandler(ClientNetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }


    //문서가 편집되었을 때 편집사항을 서버로 전송
    public void sendDeleteText(int from, int to) {
        networkHandler.sendOperation(new Operation(OperationType.DELETE, EditPosition.CONTENT,from,""));
    }
    public void sendInsertText(int from, int to, String text) {
        networkHandler.sendOperation(new Operation(OperationType.INSERT, EditPosition.CONTENT,from,text));
    }


    class networkListener implements ClientNetworkListener {
        @Override
        public void onNetworkError(Throwable t) {

        }

        @Override
        public void onOperationReceived(Operation op) {

        }

        @Override
        public <T> void onCommandSuccess(ClientCommand command, T payload) {
            //오퍼레이션만 처리
        }

        @Override
        public void onCommandFailure(ClientCommand command, String errorMessage) {

        }
    }
}
