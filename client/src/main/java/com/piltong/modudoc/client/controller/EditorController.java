package com.piltong.modudoc.client.controller;


import com.piltong.modudoc.client.view.DirectoryDialog;
import com.piltong.modudoc.client.view.EditorView;
import com.piltong.modudoc.client.network.NetworkHandler;
import com.piltong.modudoc.client.model.*;
import com.piltong.modudoc.common.model.OperationType;
import com.piltong.modudoc.common.network.ClientCommand;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fxmisc.richtext.InlineCssTextArea;
import org.fxmisc.richtext.model.StyleSpan;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class EditorController {

    // 로거
    private static final Logger log = LogManager.getLogger(EditorController.class);

    // 모델
    private Document document;

    // 컨트롤러, 뷰
    private final MainController mainController;
    private final NetworkHandler networkHandler;
    private final EditorView editorView;

    // 오퍼레이션 리스트, 프로그래밍적 적용 여부(내부 Operation 적용 여부)
    private boolean programmaticChange = false;
    private List<Operation> operations;

    // 생성자
    public EditorController(MainController mainController, NetworkHandler networkHandler) {
        this.mainController = mainController;
        this.networkHandler = networkHandler;
        this.editorView = new EditorView();
        initListeners();
    }

    // 뷰 출력
    public Parent getView() {
        return this.editorView.getRoot();
    }


    // 문서 설정
    public void setDocument(Document document) {
        this.document = document;
    }


    // 내용 설정
    public void setContent(String content) {
        programmaticChange = true;
        editorView.getEditor().clear();
        editorView.getEditor().replaceText(content);
        programmaticChange = false;
    }

    // 문서 리턴
    public Document getDocument() {
        return this.document;
    }


    // 리스너 초기화
    public void initListeners() {
        this.editorView.getBackButton().setOnAction(event->{
            mainController.reshowDashboard();
        });
        this.editorView.getSaveButton().setOnAction(event->{
           saveFile(document);
        });
        this.editorView.getEditor().richChanges()
                .filter(ch -> (!ch.getInserted().getText().isEmpty() || !ch.getRemoved().getText().isEmpty())&&!programmaticChange)
                .subscribe(change -> {
                    document.setContent(editorView.getEditor().getText());
                    int from = change.getPosition();
                    String insertedText = change.getInserted().getText();
                    String removedText = change.getRemoved().getText();
                    if(!insertedText.equals(removedText)) {
                        if(!removedText.isEmpty()) {
                            log.info(removedText+" position : "+from);
                            sendDeleteText(removedText,from);
                        }
                        if(!insertedText.isEmpty()) {
                            log.info(insertedText+" position : "+from);
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
        Platform.runLater(()->{
            programmaticChange = true;
            editorView.getEditor().insertText(from,text);
            programmaticChange = false;

        });
    }
    public void deleteText(String text,int from) {
        Platform.runLater(()->{
            programmaticChange = true;
            editorView.getEditor().deleteText(from,from+text.length());
            programmaticChange = false;
        });

    }

    public void saveFile(Document document) {
        DirectoryDialog dlg = new DirectoryDialog();
        dlg.getFileChooser().setInitialFileName(document.getTitle());
        File saveFile = dlg.getFileChooser().showSaveDialog(mainController.getStage());
        try {
            OutputStream output = new FileOutputStream(saveFile);
            output.write(document.getContent().getBytes());
            output.close();
        }catch (Exception e) {
            log.error(e);
        }
    }

}
