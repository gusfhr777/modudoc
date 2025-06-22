package com.piltong.modudoc.client.view;


import com.piltong.modudoc.client.controller.DashboardController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


// Dashboard에서 문서를 생성하는 창
public class CreateDocumentDialog {
    public GridPane getGrid() {
        return grid;
    }

    // UI 객체
    GridPane grid = new GridPane();
    Label nameLabel = new Label();

    public TextField getTitleField() {
        return titleField;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Label getNameLabel() {return nameLabel;}

    TextField titleField = new TextField(); // 문서 제목

    public void setPrompt(String text) {
        this.promptLabel.setText(text);
    }

    Label promptLabel = new Label();
    Button editButton = new Button();

    public CreateDocumentDialog() {
        grid.add(nameLabel, 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(editButton, 0, 1);
        grid.add(promptLabel, 1, 1);
        grid.setHgap(10);
    }





}
