package com.piltong.modudoc.client.controller;

import com.piltong.modudoc.client.network.ClientNetworkListener;
import com.piltong.modudoc.client.network.NetworkHandler;
import com.piltong.modudoc.client.view.DashboardView;
import com.piltong.modudoc.client.view.MainView;
import com.piltong.modudoc.common.model.OperationType;
import com.piltong.modudoc.client.model.*;

import com.piltong.modudoc.common.network.ClientCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;



// 컨트롤러에서 네트워크 인터페이스를 담당한다.
// ClientNetworkListener 인터페이스를 사용한다.
public class NetworkService implements ClientNetworkListener {

    // 로거
    private static final Logger log = LogManager.getLogger(NetworkService.class);

    // 컨트롤러, 뷰, 네트워크 핸들러
    private MainController mainController;
    private MainView mainView;
    private NetworkHandler networkHandler;



    // 생성자
    public NetworkService(MainController mainController) {
        this.mainController = mainController;
    }

    // 뷰 설정
    public void setView(MainView mainView) {
        this.mainView = mainView;
    }





    // 서버와 접속한다.
    public boolean connect(String host, int port) {

        if(host == null || host.isEmpty() || port <= 0 || port > 65535) {
            String msg = "Invalid Argument : host or port";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }


        try {
            this.networkHandler = new NetworkHandler(host, port, this);
            new Thread(networkHandler).start();
            
//            networkService.setDocumentListController(dashboardController);
//            DashboardView dashboardView = new DashboardView();
//            dashboardView.setController(dashboardController);
//            dashboardController.setView(dashboardView);
            mainController.
            
            
            dashboardView.showView();

            dashboardController.start();
            loginScene.closeView();
        }catch (RuntimeException e) {
            System.out.println("Error: "+e.getMessage());
            loginScene.setPromptText("Error: "+e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public <T> void onCommandSuccess(ClientCommand command, T payload) {
        if(dashboardController != null) {
            // 성공 응답
            switch (command) {

                // 문서 생성 명령
                case CREATE_DOCUMENT:
                    dashboardController.addDocument((Document) payload);
                    break;

                // 단일 문서 조회 명령
                case READ_DOCUMENT:
                    dashboardController.connectDocument((Document) payload);
                    break;

                // 문서 수정 명령
                case UPDATE_DOCUMENT:

                    break;

                // 문서 삭제 명령
                case DELETE_DOCUMENT:
                    break;

                // 문서 리스트 조회 명령
                case READ_DOCUMENT_LIST:
                    dashboardController.loadDocumentList((List<Document>) payload);
                    break;

                // Operation 전파 명령
                case PROPAGATE_OPERATION:

                    break;

                // 이외 명령
                default:
                    log.error("Unknown Command Received: " + command);
                    break;
            }
        }
        else
            log.error("NetworkListener not called");
    }

    @Override
    public void onOperationReceived(Operation op) {
        if(editorController != null&& editorController.getDocument().getId() == op.getDocId()) {
            if (op.getOperationType() == OperationType.INSERT) {
                editorController.insertText(op.getContent(), op.getPosition());
            } else if (op.getOperationType() == OperationType.DELETE) {
                editorController.deleteText(op.getPosition(), op.getContent().length());
            }
        }
    }

    @Override
    public void onCommandFailure(ClientCommand command, String errorMessage) {

    }

    @Override
    public void onNetworkError(Throwable t) {

    }

//
//    public void setDocumentListController(DashboardController dashboardController) {
//        this.dashboardController = dashboardController;
//    }
//    public void deleteDocumentListController() {
//        this.dashboardController = null;
//    }
//    public void setTextEditorController(EditorController editorController) {
//        this.editorController = editorController;
//    }
//    public void deleteTextEditorController() {
//        this.editorController = null;
//    }
}
