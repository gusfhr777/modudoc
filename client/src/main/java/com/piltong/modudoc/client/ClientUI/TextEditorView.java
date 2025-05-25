package com.piltong.modudoc.client.ClientUI;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import org.fxmisc.richtext.InlineCssTextArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpan;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.fxmisc.richtext.model.StyledDocument;

import com.piltong.modudoc.common.Document;


public class TextEditorView {
    InlineCssTextArea editor = new InlineCssTextArea();
    Button boldButton = new Button("Bold");
    Button underbarButton = new Button("Underbar");
    ColorPicker colorPicker = new ColorPicker();

    ComboBox<Integer> fontSizeBox = new ComboBox<>();
    TextEditorView(Document document) {
        initLayout();


    }

    void initLayout() {
        editor.setStyle("-fx-font-family: Arial;");
        editor.setParagraphGraphicFactory(LineNumberFactory.get(editor));

        colorPicker.getStyleClass().add("split-button");
        for (int size : new int[]{8, 10, 12, 14, 16, 18, 24, 32, 40}) {
            fontSizeBox.getItems().add(size);
        }
        fontSizeBox.setValue(14);
        ToolBar toolbar1 = new ToolBar();
        ToolBar toolBar = new ToolBar(boldButton, underbarButton,colorPicker, fontSizeBox);
        VBox root = new VBox();
        root.getChildren().addAll(toolBar,editor);
        VBox.setVgrow(editor, Priority.ALWAYS);
        Scene scene = new Scene(root,800,600);
    }
    void initListeners() {
        boldButton.setOnAction(e -> {
            toggleCssStyle(editor, "-fx-font-weight: BOLD");

        });
        underbarButton.setOnAction(e -> {
            toggleCssStyle(editor,"-fx-underline: true;");
        });
        colorPicker.setOnAction(e -> {
            Color selectedColor = colorPicker.getValue();
            String colorHex = toCssColor(selectedColor);
            addCssStyle(editor,"-fx-fill",colorHex);

        });
        fontSizeBox.setOnAction(e-> {
            int size = fontSizeBox.getValue();
            addCssStyle(editor,"-fx-font-size",size+"px");
        });


        editor.richChanges()
                .filter(ch -> !ch.getInserted().getText().isEmpty() || !ch.getRemoved().getText().isEmpty())
                .subscribe(change -> {
                    StyledDocument<String, String, String> inserted = change.getInserted();
                    StyledDocument<String, String, String> removed = change.getRemoved();
                    int from = change.getPosition();
                    int to = from + change.getNetLength(); // netLength = inserted - removed
                    String insertedText = inserted.getText();
                    String removedText = removed.getText();

                    boolean textChanged = !inserted.getText().equals(removed.getText());
                    boolean styleChanged = !inserted.getStyleSpans(0, inserted.length())
                            .equals(removed.getStyleSpans(0, removed.length()));

                    if(textChanged){
                        if(!removed.getText().isEmpty()){
                            System.out.println("remove: " + removedText);

                        }
                        if(!inserted.getText().isEmpty()){

                            int styleSpans = inserted.getStyleSpans(0,inserted.length()).getSpanCount();
                            System.out.println("inserted text: " + insertedText);
                            System.out.println("styleSpans: "+styleSpans);
                            for(int i = 0;i<inserted.length(); i+=inserted.getStyleRangeAtPosition(i).getLength()){
                                System.out.println("i "+i+", style: "+inserted.getStyleAtPosition(i)+", length: "+inserted.getStyleRangeAtPosition(i).getLength());
                                if(inserted.getStyleRangeAtPosition(i).getLength()==0) i++;
                            }
                        }
                    }
                    else if(styleChanged){

                        int styleSpans = inserted.getStyleSpans(0,change.getNetLength()).getSpanCount();
                        to+=inserted.length();
                        System.out.println("styleSpans: "+styleSpans);
                        for(int i = 0;i<inserted.length(); i+=change.getInserted().getStyleRangeAtPosition(i).getLength()){
                            System.out.println("style: "+inserted.getStyleAtPosition(i)+", length: "+inserted.getStyleRangeAtPosition(i).getLength());
                        }
                    }

                    System.out.println("start: " + from);
                    System.out.println("end: " + to);

                    //System.out.println("inserted style: " + insertedStyle);
                    System.out.println("");
                });
    }
    void toggleCssStyle(InlineCssTextArea area, String cssFragment) {
        int start = area.getSelection().getStart();
        int end = area.getSelection().getEnd();


        StyleSpans<String> spans = area.getStyleSpans(start, end);

        boolean allHasStyle = spans.stream()
                .allMatch(span -> span.getStyle().contains(cssFragment));

        StyleSpansBuilder<String> builder = new StyleSpansBuilder<>();
        for (StyleSpan<String> span : spans) {
            String oldStyle = span.getStyle();
            String newStyle;

            if (allHasStyle) {
                // 제거
                newStyle = oldStyle.replace(cssFragment, "").trim();
            } else {
                // 추가 (중복 방지)
                if (oldStyle.contains(cssFragment)) {
                    newStyle = oldStyle;
                } else {
                    newStyle = oldStyle.isEmpty() ? cssFragment : oldStyle + " " + cssFragment;
                }
            }

            builder.add(new StyleSpan<>(newStyle, span.getLength()));
        }

        area.setStyleSpans(start, builder.create());
    }
    void addCssStyle(InlineCssTextArea area, String cssFragment, String value) {
        int start = area.getSelection().getStart();
        int end = area.getSelection().getEnd();



        StyleSpans<String> spans = area.getStyleSpans(start, end);

        boolean allHasStyle = spans.stream()
                .allMatch(span -> span.getStyle().contains(cssFragment+value));

        StyleSpansBuilder<String> builder = new StyleSpansBuilder<>();
        for (StyleSpan<String> span : spans) {
            String oldStyle = span.getStyle();
            String newStyle= mergeOrReplaceStyle(oldStyle,cssFragment,value);

            builder.add(new StyleSpan<>(newStyle, span.getLength()));
        }

        area.setStyleSpans(start, builder.create());
    }
    String toCssColor(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
    String mergeOrReplaceStyle(String original, String key, String value) {
        if (original == null) original = "";
        String[] parts = original.split(";");
        StringBuilder result = new StringBuilder();
        boolean replaced = false;

        for (String part : parts) {
            part = part.trim();
            if (part.startsWith(key)) {
                result.append(key).append(": ").append(value).append("; ");
                replaced = true;
            } else if (!part.isEmpty()) {
                result.append(part).append("; ");
            }
        }

        if (!replaced) {
            result.append(key).append(": ").append(value).append("; ");
        }

        return result.toString().trim();
    }
}
