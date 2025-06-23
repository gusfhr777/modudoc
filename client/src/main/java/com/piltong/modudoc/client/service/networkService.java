package com.piltong.modudoc.client.service;

import com.piltong.modudoc.client.controller.MainController;
import com.piltong.modudoc.client.network.NetworkHandler;
import com.piltong.modudoc.client.model.*;

import com.piltong.modudoc.client.network.networkHandlerListener;
import com.piltong.modudoc.common.model.OperationType;
import com.piltong.modudoc.common.network.ClientCommand;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;



// 컨트롤러에서 네트워크 인터페이스를 담당한다.
// ClientNetworkListener 인터페이스를 사용한다.
public class networkService implements networkHandlerListener {

    // 로거
    private static final Logger log = LogManager.getLogger(networkService.class);

    // 컨트롤러
    private final MainController mainController;
    private NetworkHandler networkHandler;

    // 생성자
    public networkService(MainController mainController) {
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
                mainController.getDashboardController().loadDashboard((List<Document>) payload);
                break;

            case PROPAGATE_OPERATION:
                break;

            case LOGIN:
                onLoginResponse((User)payload);
                break;

            default:
                break;
        }
    }


    // 요청 별 처리 함수

    public void onLoginResponse(User payload) {
        if(payload.getId()==null||payload.getPassword()==null||payload.getUsername()==null) {
            log.info("Login Failed");
            mainController.getLoginController().setPrompt("아이디나 비밀번호가 일치하지 않습니다.");
            return;
        }
        log.info("Login Success");
        networkHandler.sendCommand(ClientCommand.READ_DOCUMENT_LIST, null);
        mainController.showDashboard();

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
    public void onOperationReceived(List<Operation> opList) {
        log.info("Operation Recevied : " + opList);
        for (Operation op : opList) {
            if(mainController.getEditorController().getDocument()!=null&&mainController.getEditorController().getDocument().getId() == op.getDocId()) {
                if (op.getOperationType() == OperationType.INSERT) {

                    mainController.getEditorController().insertText(op.getContent(), op.getPosition());
                } else if (op.getOperationType() == OperationType.DELETE) {
                    mainController.getEditorController().deleteText(op.getContent(), op.getPosition());
                }
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
