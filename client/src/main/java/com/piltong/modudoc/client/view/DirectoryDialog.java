package com.piltong.modudoc.client.view;

import javafx.stage.FileChooser;

public class DirectoryDialog {


    FileChooser fileChooser;


    public FileChooser getFileChooser() {
        return fileChooser;
    }

    public DirectoryDialog() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a directory");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
    }
}
