package com.piltong.modudoc.client.view;

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

import com.piltong.modudoc.common.document.Document;

//텍스트 편집기를 생성
public class TextEditorView {
    InlineCssTextArea editor = new InlineCssTextArea();
    Button boldButton = new Button("Bold");
    Button underbarButton = new Button("Underbar");
    ColorPicker colorPicker = new ColorPicker();
    ToolBar toolBar = new ToolBar();
    VBox root = new VBox();
    ComboBox<Integer> fontSizeBox = new ComboBox<>();


    public TextEditorView(Document document) {
        initComponent(document);
        initLayout();
        initListeners();
    }

    void initComponent(Document document) {
        editor.setStyle("-fx-font-family: Arial; -fx-font-scale: 14;");
        editor.setParagraphGraphicFactory(LineNumberFactory.get(editor));
        for (int size : new int[]{8, 10, 12, 14, 16, 18, 24, 32, 40}) {
            fontSizeBox.getItems().add(size);
        }
        fontSizeBox.setValue(14);

    }

    void initLayout() {

        toolBar = new ToolBar(boldButton, underbarButton,colorPicker, fontSizeBox);
        root = new VBox();
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


        //추가 혹은 삭제된 텍스트가 있을 시(텍스트 변화 시) 이벤트 발생
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
                            //텍스트 제거시 이벤트

                        }
                        if(!inserted.getText().isEmpty()){
                            //텍스트 추가시 이벤트

                        }
                    }
                    else if(styleChanged){
                        //스타일만 변화했을 때 이벤트

                    }


                });
    }

    //토글 형식의 스타일 변경용 메소드
    //볼드, 및줄과 같이 버튼식으로 css적용시 사용
    void toggleCssStyle(InlineCssTextArea area, String cssFragment) {
        int start = area.getSelection().getStart();
        int end = area.getSelection().getEnd();


        StyleSpans<String> spans = area.getStyleSpans(start, end);

        //이미 해당 스타일을 보유하고 있는지 확인
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

    //스타일을 새롭게 추가할 때 사용하는 메소드
    //기존에 스타일이 존재할 시 변경하지 않는다.
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

    //
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
    void insertText(StyledDocument<String,String,String> styledDocument, int start) {
        editor.insert(start, styledDocument);
    }

    StyledDocument<String,String,String> getStyledDocument(int start,int end) {
        return editor.getDocument().subSequence(start, end);
    }
    void deleteText(int start,int end) {
        editor.deleteText(start, end);
    }
    void modifyText(StyledDocument<String,String,String> styledDocument,int start,int end) {
        deleteText(start,end);
        insertText(styledDocument,start);
    }
    void ggg() {

    }
}
