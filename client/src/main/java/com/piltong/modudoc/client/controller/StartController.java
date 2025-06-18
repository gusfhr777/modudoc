package com.piltong.modudoc.client.controller;

import com.piltong.modudoc.client.network.NetworkListener;
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
    public void connect(String host, String Stringport) {
        if(host == null || host.isEmpty()) {
            host = "localhost";
        }
        else {
            this.host = host;
        }


        try {
            port = Integer.parseInt(Stringport);
        }
        catch (NumberFormatException e) {
            port = 8080;
            e.printStackTrace();
        }


        if(port <= 0|| port > 65535) {
            port = 8080;
        }
        else {
            this.port = port;
        }

        try {
            NetworkListener networkListener = new NetworkListener();
            networkHandler = new ClientNetworkHandler(this.host,port,networkListener);
            DocumentListController documentListController = new DocumentListController(networkHandler);
            DocumentListView documentListView = new DocumentListView();
            documentListView.setController(documentListController);
            documentListController.setView(documentListView);
            documentListView.showView();
            documentListController.start();
            startView.closeView();
        }catch (RuntimeException e) {
            System.out.println("Error: "+e.getMessage());
        }
    }

}
