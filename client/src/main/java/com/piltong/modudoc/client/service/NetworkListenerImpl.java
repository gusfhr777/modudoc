package com.piltong.modudoc.client.service;

import com.piltong.modudoc.client.controller.MainController;
import com.piltong.modudoc.client.network.NetworkHandler;
import com.piltong.modudoc.client.model.*;

import com.piltong.modudoc.common.model.OperationType;
import com.piltong.modudoc.common.network.ClientCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;



// 컨트롤러에서 네트워크 인터페이스를 담당한다.
// ClientNetworkListener 인터페이스를 사용한다.
public class NetworkListenerImpl implements ClientNetworkListener {

    // 로거
    private static final Logger log = LogManager.getLogger(NetworkListenerImpl.class);

    // 컨트롤러
    private final MainController mainController;
    private NetworkHandler networkHandler;

    // 생성자
    public NetworkListenerImpl(MainController mainController) {
        this.mainController = mainController;
    }

    // 의존성 주입
    public void setNetworkHandler(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    // 뷰 설정
//    public void setView(View view) {
//        this.mainView = (MainView) view;
//
//    }
//
//    public void start() {
//
//    }
//
//    public void end() {
//
//    }
//
//    public void shutdown() {
//
//    }
//




    // 서버와 접속한다.
//    public boolean connect(String host, int port) {
//
//        if(host == null || host.isEmpty() || port <= 0 || port > 65535) {
//            String msg = "Invalid Argument : host or port";
//            log.error(msg);
//            throw new IllegalArgumentException(msg);
//        }
//
//
//        try {
//            this.networkHandler = new NetworkHandler(host, port, this);
//            new Thread(networkHandler).start();
//
////            networkService.setDocumentListController(dashboardController);
////            DashboardView dashboardView = new DashboardView();
////            dashboardView.setController(dashboardController);
////            dashboardController.setView(dashboardView);
//
//
////            dashboardView.showView();
////
////            dashboardController.start();
////            loginScene.closeView();
//            return true;
//        }catch (RuntimeException e) {
//            System.out.println("Error: "+e.getMessage());
////            loginView.setPromptText("Error: "+e.getMessage());
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }


    @Override
    public <T> void onCommandSuccess(ClientCommand command, T payload) {
        log.info("Successful command Recevied : " + command);
        switch (command) {
            case CREATE_DOCUMENT:
                mainController.getDashboardController().addDocument((Document) payload);
                break;

            case READ_DOCUMENT:
                mainController.showEditor((Document) payload);
                //mainController.getEditorController().loadDocument((Document) payload);
                break;

            case UPDATE_DOCUMENT:
                break;

            case DELETE_DOCUMENT:
                break;

            case READ_DOCUMENT_LIST:
                mainController.getDashboardController().loadDocumentList((List<Document>) payload);
                break;

            case PROPAGATE_OPERATION:
                break;

            case LOGIN:
                networkHandler.sendCommand(ClientCommand.READ_DOCUMENT_LIST, null);
                mainController.showDashboard();
                break;

            default:
                break;
        }


//        if(mainController.getDashboardController() != null) {
//            // 성공 응답
//            switch (command) {
//
//                // 문서 생성 명령
//                case CREATE_DOCUMENT:
//                    mainController.getDashboardController().addDocument((Document) payload);
//                    break;
//
//                // 단일 문서 조회 명령
//                case READ_DOCUMENT:
//                    mainController.getDashboardController().connectDocument((Document) payload);
//                    break;
//
//                // 문서 수정 명령
//                case UPDATE_DOCUMENT:
//
//                    break;
//
//                // 문서 삭제 명령
//                case DELETE_DOCUMENT:
//                    break;
//
//                // 문서 리스트 조회 명령
//                case READ_DOCUMENT_LIST:
//                    mainController.getDashboardController().loadDocumentList((List<Document>) payload);
//                    break;
//
//                // Operation 전파 명령
//                case PROPAGATE_OPERATION:
//
//                    break;
//
//                // 이외 명령
//                default:
//                    log.error("Unknown Command Received: " + command);
//                    break;
//            }
//        }
//        else
//            log.error("NetworkListener not called");
    }

    @Override
    public void onOperationReceived(Operation op) {
        if(mainController.getEditorController().getDocument().getId() == op.getDocId()) {
            if (op.getOperationType() == OperationType.INSERT) {
                mainController.getEditorController().insertText(op.getContent(), op.getPosition());
            } else if (op.getOperationType() == OperationType.DELETE) {
                mainController.getEditorController().deleteText(op.getContent(), op.getPosition());
            }
        }
    }

    @Override
    public void onCommandFailure(ClientCommand command, String errorMessage) {
        log.error("Failed Command Received : " + command + " : " + errorMessage);
    }

    @Override
    public void onNetworkError(Throwable t) {
        log.error("Network Error Received : " + t.getMessage());
        t.printStackTrace();

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
