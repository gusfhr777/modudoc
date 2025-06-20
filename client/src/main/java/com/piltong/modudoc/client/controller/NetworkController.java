package com.piltong.modudoc.client.controller;

import com.piltong.modudoc.client.network.ClientNetworkListener;
import com.piltong.modudoc.common.model.OperationType;
import com.piltong.modudoc.client.model.*;

import com.piltong.modudoc.common.network.ClientCommand;

import java.util.List;

public class NetworkController implements ClientNetworkListener {

    DocumentListController documentListController;
    TextEditorController textEditorController;

    @Override
    public <T> void onCommandSuccess(ClientCommand command, T payload) {
        if(documentListController != null) {
            // 성공 응답
            switch (command) {

                // 문서 생성 명령
                case CREATE_DOCUMENT:
                    documentListController.addDocument((Document) payload);
                    break;

                // 단일 문서 조회 명령
                case READ_DOCUMENT:
                    documentListController.connectDocument((Document) payload);
                    break;

                // 문서 수정 명령
                case UPDATE_DOCUMENT:

                    break;

                // 문서 삭제 명령
                case DELETE_DOCUMENT:
                    break;

                // 문서 리스트 조회 명령
                case READ_DOCUMENT_LIST:
                    documentListController.loadDocumentList((List<Document>) payload);
                    break;

                // Operation 전파 명령
                case PROPAGATE_OPERATION:

                    break;

                // 이외 명령
                default:
                    System.err.println("Unknown Command Received: " + command);
                    break;
            }
        }
        else
            System.err.println("NetworkListener not called");
    }

    @Override
    public void onOperationReceived(Operation op) {
        if(textEditorController != null&& textEditorController.getDocument().getId() == op.getDocId()) {
            if (op.getOperationType() == OperationType.INSERT) {
                textEditorController.insertText(op.getContent(), op.getPosition());
            } else if (op.getOperationType() == OperationType.DELETE) {
                textEditorController.deleteText(op.getPosition(), op.getContent().length());
            }
        }
    }

    @Override
    public void onCommandFailure(ClientCommand command, String errorMessage) {

    }

    @Override
    public void onNetworkError(Throwable t) {

    }

    public void setDocumentListController(DocumentListController documentListController) {
        this.documentListController = documentListController;
    }
    public void deleteDocumentListController() {
        this.documentListController = null;
    }
    public void setTextEditorController(TextEditorController textEditorController) {
        this.textEditorController = textEditorController;
    }
    public void deleteTextEditorController() {
        this.textEditorController = null;
    }
}
