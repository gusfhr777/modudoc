package com.piltong.modudoc.client.view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.stage.Stage;
import org.fxmisc.richtext.InlineCssTextArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpan;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.fxmisc.richtext.model.StyledDocument;

import com.piltong.modudoc.common.document.Document;

//텍스트 편집기를 생성
public class TextEditorView {
    InlineCssTextArea editor = new InlineCssTextArea(); //텍스트를 입력할 수 있는 영역
    Button boldButton = new Button("Bold"); //문자의 볼드체 적용 여부를 선택하는 버튼
    Button underbarButton = new Button("Underbar"); //문자의 및줄 적용 여부를 선택하는 버튼
    ColorPicker colorPicker = new ColorPicker(); //문자의 색깔을 선택하는 선택기
    ToolBar toolBar = new ToolBar(); //텍스트 편집에 사용될 요소들이 들어갈 창
    VBox root = new VBox(); //구성요소들을 세로로 배치하는 레이아웃
    ComboBox<Integer> fontSizeBox = new ComboBox<>(); //폰트 크기를 설정하는 선택기


    Stage textEditorStage = new Stage();


    //구성요소들을 초기화하는 메소드
    void initComponent(Document document) {
        //기본 스타일 설정
        editor.setStyle("-fx-font-family: Arial; -fx-font-scale: 14;");
        //왼쪽에 텍스트 줄 표시
        editor.setParagraphGraphicFactory(LineNumberFactory.get(editor));
        //폰트 선택 박스 설정
        for (int size : new int[]{8, 10, 12, 14, 16, 18, 24, 32, 40}) {
            fontSizeBox.getItems().add(size);
        }
        //기본 폰트 값 설정
        fontSizeBox.setValue(14);
        //내부 텍스트 설정
        //editor.insert(0,document.getContent().length(),document.getContent());

    }

    //구성요소들을 배치하는 메소드
    void initLayout() {

        toolBar = new ToolBar(boldButton, underbarButton,colorPicker, fontSizeBox);
        root = new VBox();
        root.getChildren().addAll(toolBar,editor);
        VBox.setVgrow(editor, Priority.ALWAYS);
        Scene scene = new Scene(root,800,600);
        textEditorStage.setScene(scene);

    }

    //이벤트를 감지하는 메소드
    void initListeners() {

        //볼드 버튼이 눌렸을 때
        boldButton.setOnAction(e -> {
            toggleCssStyle(editor, "-fx-font-weight: BOLD");

        });
        //및줄 버튼이 눌렸을 때
        underbarButton.setOnAction(e -> {
            toggleCssStyle(editor,"-fx-underline: true;");
        });

        //색 선택기에서 색을 선택했을 때
        colorPicker.setOnAction(e -> {
            Color selectedColor = colorPicker.getValue();
            String colorHex = toCssColor(selectedColor);
            addCssStyle(editor,"-fx-fill",colorHex);

        });

        //폰트를 선택했을 떄
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


                    boolean textChanged = !insertedText.equals(removedText);
                    boolean styleChanged = !inserted.getStyleSpans(0, inserted.length())
                            .equals(removed.getStyleSpans(0, removed.length()));

                    if(textChanged){
                        if(!removedText.isEmpty()&& insertedText.isEmpty()){
                            //텍스트 제거만 일어났을 때 이벤트

                        }
                        else if(!insertedText.isEmpty() && removedText.isEmpty()){
                            //텍스트 추가만 일어났을 때 이벤트

                        }
                        else if(!insertedText.isEmpty()&&!removedText.isEmpty()) {
                            //텍스트 추가와 제거가 동시에 일어났을 때
                        }
                    }
                    else if(styleChanged){
                        //스타일만 변화했을 때 이벤트

                    }


                });
    }


    public void showView() {
        textEditorStage.show();
    }
    void closeView() {
        textEditorStage.close();
    }

    //토글 형식의 스타일 변경용 메소드
    //볼드, 및줄과 같이 버튼식으로 css적용시 사용
    void toggleCssStyle(InlineCssTextArea area, String cssFragment) {
        int start = area.getSelection().getStart();
        int end = area.getSelection().getEnd();


        StyleSpans<String> spans = area.getStyleSpans(start, end);

        //이미 모든 영역이 해당 스타일을 보유하고 있는지 확인
        boolean allHasStyle = spans.stream()
                .allMatch(span -> span.getStyle().contains(cssFragment));

        StyleSpansBuilder<String> builder = new StyleSpansBuilder<>();

        //각 스타일 영역별로 스타일 적용
        for (StyleSpan<String> span : spans) {
            String oldStyle = span.getStyle();
            String newStyle;

            if (allHasStyle) {
                // 모든 영역이 스타일 보유시 삭제
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

        //선택 구간의 스타일 추출
        StyleSpans<String> spans = area.getStyleSpans(start, end);

        //선택 구간이 모두 추가하길 원하는 스타일을 보유하고 있는지 확인
        boolean allHasStyle = spans.stream()
                .allMatch(span -> span.getStyle().contains(cssFragment+value));

        StyleSpansBuilder<String> builder = new StyleSpansBuilder<>();
        //모든 영역이 스타일을 보유하고 있지 않을 시 스타일 추가 실행
        if(!allHasStyle) {
            for (StyleSpan<String> span : spans) {
                String oldStyle = span.getStyle();
                String newStyle= mergeOrReplaceStyle(oldStyle,cssFragment,value);

                builder.add(new StyleSpan<>(newStyle, span.getLength()));
            }
        }


        area.setStyleSpans(start, builder.create());
    }

    //색깔 선택기에서 선택한 색을 css 형식으로 변환하는 메소드
    String toCssColor(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    //입력한 스타일이 이미 있는지 확인하고 없을 시 적용
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


    //입력받은 영역의 문자 및 스타일 반환
    StyledDocument<String,String,String> getStyledDocument(int start,int end) {
        return editor.getDocument().subSequence(start, end);
    }

    //텍스트를 위치에 추가
    void insertText(StyledDocument<String,String,String> styledDocument, int start) {
        editor.insert(start, styledDocument);
    }

    //입력받은 영역의 텍스트 삭제
    void deleteText(int start,int end) {
        editor.deleteText(start, end);
    }
    //입력받은 영역의 텍스트 입력받은 텍스트로 바꾸기
    void modifyText(StyledDocument<String,String,String> styledDocument,int start,int end) {
        deleteText(start,end);
        insertText(styledDocument,start);
    }
}