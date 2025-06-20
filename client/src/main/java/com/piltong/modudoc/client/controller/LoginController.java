package com.piltong.modudoc.client.controller;

import com.piltong.modudoc.client.view.DashboardScene;
import com.piltong.modudoc.client.view.LoginScene;
import com.piltong.modudoc.client.network.ClientNetworkHandler;

import java.io.IOException;

public class LoginController {
    private LoginScene loginScene;

    //생성자,
    public LoginController(LoginScene startview) {
        this.loginScene = startview;
        this.loginScene.initialize();
    }
    public LoginController() {}



    public void setView(LoginScene loginScene) {
        this.loginScene = loginScene;
        this.loginScene.initialize();
    }


    //연결 버튼을 눌렀을 때 호출되는 메소드
    public void connect(String host, String Stringport) {
        if(host == null || host.isEmpty()) {
            this.host = "localhost";
        }
        else {
            this.host = host;
        }


        try {
            port = Integer.parseInt(Stringport);
        }
        catch (NumberFormatException e) {
            port = 4433;
            e.printStackTrace();
        }


        if(port <= 0|| port > 65535) {
            port = 4433;
        }

        try {
            NetworkController networkController = new NetworkController();
            ClientNetworkHandler networkHandler = new ClientNetworkHandler(this.host,port, networkController);
            DashboardController dashboardController = new DashboardController(networkHandler);
            networkController.setDocumentListController(dashboardController);
            DashboardScene dashboardScene = new DashboardScene();
            dashboardScene.setController(dashboardController);
            dashboardController.setView(dashboardScene);
            dashboardScene.showView();
            dashboardController.start();
            loginScene.closeView();
        }catch (RuntimeException e) {
            System.out.println("Error: "+e.getMessage());
            loginScene.setPromptText("Error: "+e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
