package com.piltong.modudoc.client.view;

import javafx.stage.FileChooser;

// 파일 저장용 다일로그
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
