package com.piltong.modudoc.client.controller;

import com.piltong.modudoc.client.model.LoginRequest;
import com.piltong.modudoc.client.network.NetworkHandler;
import com.piltong.modudoc.client.view.LoginView;
import com.piltong.modudoc.common.network.ClientCommand;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// LoginController.java
import javafx.scene.Parent;


public class LoginController{
    private static final Logger log = LogManager.getLogger(LoginController.class);
    private final MainController mainController;
    private final NetworkHandler networkHandler;
    private final LoginView loginView;

    public LoginController(MainController mainController, NetworkHandler networkHandler) {
        this.mainController = mainController;
        this.networkHandler = networkHandler;
        this.loginView = new LoginView();

        loginView.getLoginButton().setOnAction(e -> {
            if (loginView.getUserId().isEmpty() || loginView.getPassword().isEmpty())
                setPrompt("아이디와 비밀번호를 입력해주세요");
            else {
                onLogin();
            }
        }
        );
    }

    public void onLogin() {
        LoginRequest payload = new LoginRequest(loginView.getUserId(), loginView.getPassword());
        networkHandler.sendCommand(ClientCommand.LOGIN, payload);
        log.info("Login Pressed.");
    }

    public Parent getView() {
        return this.loginView.getRoot();
    }

    public void setPrompt(String prompt) {
        Platform.runLater(() -> {
            loginView.setPrompt(prompt);});
    }

}
