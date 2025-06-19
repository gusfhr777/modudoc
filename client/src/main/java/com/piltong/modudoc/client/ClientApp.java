package com.piltong.modudoc.client;

import com.piltong.modudoc.common.Constants;
import com.piltong.modudoc.client.view.StartView;
import com.piltong.modudoc.client.controller.StartController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientApp extends Application {
    public static void main(String[] args) {
        System.out.println("환영합니다!");
//        System.out.println("SERVER_IP : " + Constants.SERVER_IP);
//        System.out.println("SERVER_PORT : " + Constants.SERVER_PORT);

        ClientApp.launch(args);

    }

    @Override
    public void start(Stage stage) throws Exception {
        StartView startView = new StartView();
        StartController startController = new StartController(startView);
        startView.setController(startController);
        startView.showView();
    }
}
