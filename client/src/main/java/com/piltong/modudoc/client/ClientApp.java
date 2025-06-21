package com.piltong.modudoc.client;

import com.piltong.modudoc.client.controller.MainController;
import com.piltong.modudoc.client.view.MainView;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientApp extends Application {
    private static final Logger log = LogManager.getLogger(ClientApp.class);


    // JavaFX Application 메서드
    @Override
    public void start(Stage stage) throws Exception {
        new MainController(stage);
        stage.setTitle("모두문서 클라이언트");
        stage.show();
    }


    public static void main(String[] args) {
        log.info("Client Started.");
        launch(args); // JavaFX 앱 초기화
    }

}
