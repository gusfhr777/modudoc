package com.piltong.modudoc.client.controller;

import com.piltong.modudoc.client.view.DocumentListView;
import com.piltong.modudoc.client.view.StartView;
import com.piltong.modudoc.client.network.ClientNetworkHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class StartController {
    private StartView startView;
    ClientNetworkHandler networkHandler;
    String host;
    int port = 8080;

    //생성자,
    public StartController(StartView startview) {
        this.startView = startview;
        this.startView.initialize();
    }
    public StartController() {}



    public void setView(StartView startView) {
        this.startView = startView;
        this.startView.initialize();
    }


    //연결 버튼을 눌렀을 때 호출되는 메소드
    public void connect(String host) {
        if(host == null || host.isEmpty()) {
            host = "localhost";
        }
        else {
            this.host = host;
        }
        try {
            networkHandler = new ClientNetworkHandler();
            DocumentListController documentListController = new DocumentListController(networkHandler);
            DocumentListView documentListView = new DocumentListView();
            documentListView.setController(documentListController);
            documentListController.setView(documentListView);
            documentListView.showView();
            startView.closeView();
        }catch (RuntimeException e) {

        }
    }

}
