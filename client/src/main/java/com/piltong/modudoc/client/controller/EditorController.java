package com.piltong.modudoc.client.controller;


import com.piltong.modudoc.client.view.EditorView;
import com.piltong.modudoc.client.network.NetworkHandler;
import com.piltong.modudoc.client.model.*;
import com.piltong.modudoc.common.model.OperationType;
import com.piltong.modudoc.common.network.ClientCommand;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fxmisc.richtext.InlineCssTextArea;
import org.fxmisc.richtext.model.StyleSpan;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

public class EditorController {

    // 모델
    private Document document;

    private static final Logger log = LogManager.getLogger(EditorController.class);

    private final MainController mainController;
    private final NetworkHandler networkHandler;
    private final EditorView editorView;


    public EditorController(MainController mainController, NetworkHandler networkHandler) {
        this.mainController = mainController;
        this.networkHandler = networkHandler;
        this.editorView = new EditorView();
        initListeners();
    }

    public Parent getView() {
        return this.editorView.getRoot();
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public void setContent(String content) {
        editorView.getEditor().replaceText(content);
    }
    public Document getDocument() {
        return this.document;
    }

    public void initListeners() {
        this.editorView.getBackButton().setOnAction(event->{
            mainController.showDashboard();
        });
        this.editorView.getBoldButton().setOnAction(event -> {
            toggleCssStyle(this.editorView.getEditor(),"-fx-font-weight: BOLD;");
        });
        this.editorView.getUnderbarButton().setOnAction(event -> {
            toggleCssStyle(this.editorView.getEditor(),"-fx-underline: true;");
        });
        this.editorView.getColorPicker().setOnAction(event -> {
            Color selectedColor = this.editorView.getColorPicker().getValue();
            addCssStyle(this.editorView.getEditor(),"-fx-fill",toCssColor(selectedColor));
        });
        this.editorView.getFontSizeBox().setOnAction(event -> {
            addCssStyle(this.editorView.getEditor(),"-fx-font-scale",this.editorView.getFontSizeBox().getValue()+"px");
        });
        this.editorView.getFontFamilyBox().setOnAction(event -> {
            addCssStyle(this.editorView.getEditor(),"-fx-font-family",this.editorView.getFontFamilyBox().getValue());
        });
        this.editorView.getEditor().richChanges()
                .filter(ch -> !ch.getInserted().getText().isEmpty() || !ch.getRemoved().getText().isEmpty())
                .subscribe(change -> {
                    int from = change.getPosition();
                    int to = from + change.getNetLength();
                    String insertedText = change.getInserted().getText();
                    String removedText = change.getRemoved().getText();

                    if(!insertedText.equals(removedText)) {
                        if(!removedText.isEmpty()) {
                            sendDeleteText(removedText,to);                        }
                        if(!insertedText.isEmpty()) {
                            sendInsertText(insertedText,from);
                        }
                    }
                });
    }

    public void sendInsertText(String text,int from) {
        networkHandler.sendCommand(ClientCommand.PROPAGATE_OPERATION,new Operation(OperationType.INSERT,document.getId(),from,text));

    }
    public void sendDeleteText(String text,int from) {
        networkHandler.sendCommand(ClientCommand.PROPAGATE_OPERATION,new Operation(OperationType.DELETE,document.getId(),from,text));

    }

    public void insertText(String text,int from) {
        editorView.getEditor().insertText(from,text);
    }
    public void deleteText(String text,int from) {
        editorView.getEditor().deleteText(from,from+text.length());
    }
    //토글 형식의 스타일 변경용 메소드
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


//
//    // 컨트롤러, 뷰
//    private MainController mainController;
//    private MainView mainView;
//    private EditorView editorView;
//    private NetworkHandler networkHandler;
//
//
//    //생성자, 뷰와 네트워크를 입력받아 생성하거나 네트워크만 입력받아 생성
//    public EditorController(MainController mainController) {
//        this.mainController = mainController;
//    }
//
//
//    public void setView(View view) {
//        this.mainView = (MainView) view;
//        this.editorView = this.mainView.getEditorView();
//    }
//
//    // 초기화
//    public void init(Document document) {
//        this.document = document;
//        this.editorView.initialize(document, this);
//    }
//
//    // 시작
//    public void start() {
//
//    }
//
//    // 끝
//    public void end() {
//
//    }
//
//    // 종료
//    public void shutdown() {
//
//    }
//
//
//
//
//
//    //네트워크 설정
//    public void setNetworkHandler(NetworkHandler networkHandler) {
//        this.networkHandler = networkHandler;
//    }
//
//    public void setDocument(Document document) {
//        this.document = document;
//    }
//    public Document getDocument() {
//        return document;
//    }
//
//    public void insertText(String text, int position) {
//        editorView.insertStringText(text,position);
//    }
//    public void deleteText(int position, int length) {
//        editorView.deleteText(position, position+length);
//    }
//
//    //문서가 편집되었을 때 편집사항을 서버로 전송
//    public void sendDeleteText(int from, int to,String text) {
//        networkHandler.sendOperation(new Operation(OperationType.DELETE,document.getId(),to,text));
//    }
//    public void sendInsertText(int from, int to, String text) {
//        networkHandler.sendOperation(new Operation(OperationType.INSERT,document.getId(), from,text));
//    }

}
