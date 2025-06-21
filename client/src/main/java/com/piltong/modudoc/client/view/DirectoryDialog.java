package com.piltong.modudoc.client.view;

import com.piltong.modudoc.client.model.Document;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

import static javafx.application.Application.launch;

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
