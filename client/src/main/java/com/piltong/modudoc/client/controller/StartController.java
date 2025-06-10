package com.piltong.modudoc.client.controller;

import com.piltong.modudoc.client.view.DocumentListView;
import com.piltong.modudoc.client.view.StartView;
import com.piltong.modudoc.client.network.ClientNetworkHandler;

public class StartController {
    private StartView startView;
    ClientNetworkHandler networkHandler;
    String host;
    int port = 8080;

    //생성자,
    public StartController(StartView startview) {
        this.startView = startview;
    }
    public StartController() {}

    public void setView(StartView startView) {
        this.startView = startView;
    }

    public void connect(String host) {
        if(host == null || host.isEmpty()) {
            host = "localhost";
        }
        else {
            this.host = host;
        }
        networkHandler = new ClientNetworkHandler();
        DocumentListController documentListController = new DocumentListController(networkHandler);
        DocumentListView documentListView = new DocumentListView();
        documentListView.setController(documentListController);
        documentListController.setView(documentListView);
        documentListView.showView();
        startView.closeView();
    }
}
