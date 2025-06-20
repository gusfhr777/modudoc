package com.piltong.modudoc.client;

import com.piltong.modudoc.client.controller.MainController;
import com.piltong.modudoc.client.view.MainView;
import com.piltong.modudoc.common.Constants;
import com.piltong.modudoc.client.view.StartView;
import com.piltong.modudoc.client.controller.StartController;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientApp extends Application {
    private static final Logger log = LogManager.getLogger(ClientApp.class);

    public static void main(String[] args) {
        log.info("환영합니다!");
        ClientApp.launch(args); // JavaFX 앱 초기화
    }


    // JavaFX Application 메서드
    @Override
    public void start(Stage stage) throws Exception {

        MainController controller = new MainController();
        MainView view = new MainView();

        view.setController(controller);
        controller.startApp();


//        StartView startView = new StartView();
//        StartController startController = new StartController(startView);
//        startView.setController(startController);
////        startView.showView();
//        startController.connect("localhost", "4433"); // 테스트용 자동 접속
    }

}
