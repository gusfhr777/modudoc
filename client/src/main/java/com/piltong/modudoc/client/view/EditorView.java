package com.piltong.modudoc.client.view;

import com.piltong.modudoc.client.controller.EditorController;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.fxmisc.richtext.InlineCssTextArea;
import org.fxmisc.richtext.model.StyleSpan;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.fxmisc.richtext.model.StyledDocument;

import com.piltong.modudoc.client.model.Document;

//텍스트 편집기를 생성
public class EditorView {
    InlineCssTextArea editor = new InlineCssTextArea(); //텍스트를 입력할 수 있는 영역

    Button backButton = new Button("🔙");
    Button saveButton = new Button("SAVE");

    Button boldButton = new Button("Bold"); //문자의 볼드체 적용 여부를 선택하는 버튼
    Button underbarButton = new Button("Underbar"); //문자의 및줄 적용 여부를 선택하는 버튼
    ColorPicker colorPicker = new ColorPicker(); //문자의 색깔을 선택하는 선택기
    ComboBox<Integer> fontSizeBox = new ComboBox<>(); //폰트 크기를 설정하는 선택기
    ComboBox<String> fontFamilyBox = new ComboBox<>();


    ToolBar menuBar;
    ToolBar toolBar; //텍스트 편집에 사용될 요소들이 들어갈 창
    VBox root = new VBox(); //구성요소들을 세로로 배치하는 레이아웃

    Stage textEditorStage = new Stage();
//

    public EditorView() {
        //기본 스타일 설정
        editor.setStyle("-fx-font-family: Arial; -fx-font-scale: 14;");
        menuBar = new ToolBar(backButton,saveButton);

        styleButton(backButton);
        styleButton(saveButton);
        styleButton(boldButton);
        styleButton(underbarButton);

        root = new VBox();
        root.getChildren().addAll(menuBar,editor);
        VBox.setVgrow(editor, Priority.ALWAYS);

    }

    // Getter
    public Parent getRoot() {
        return root;
    }

    public InlineCssTextArea getEditor() {return editor;}

    public Button getBackButton() {return backButton;}
    public Button getSaveButton() {return saveButton;}
    public Button getBoldButton() {return boldButton;}
    public Button getUnderbarButton() {return underbarButton;}
    public ColorPicker getColorPicker() {return colorPicker;}
    public ComboBox<Integer> getFontSizeBox() {return fontSizeBox;}
    public ComboBox<String> getFontFamilyBox() {return fontFamilyBox;}

    private void styleButton(Button button) {
        String defaultStyle =
                "-fx-background-color: #42A5F5;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-padding: 6 14 6 14;";

        String hoverStyle =
                "-fx-background-color: #1E88E5;" +  // hover 시 진한 파랑
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-padding: 6 14 6 14;";

        button.setStyle(defaultStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(defaultStyle));
    }
}