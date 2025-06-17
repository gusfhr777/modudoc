package com.piltong.modudoc.common.operation;

import java.io.Serializable;

public class OperationDto implements Serializable {

    private OperationType operationType; // 삽입, 삭제 등 연산 종류
    private String documentId;           // 대상 문서 ID
    private int position;                // 연산 위치
    private String content;              // 삽입/삭제할 내용

    // 생성자
    public OperationDto(OperationType operationType, String documentId, int position, String content) {
        this.operationType = operationType;
        this.documentId = documentId;
        this.position = position;
        this.content = content;
    }

    // Getter
    public OperationType getOperationType() {
        return operationType;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getPosition() {
        return position;
    }

    public String getContent() {
        return content;
    }
}
