package com.piltong.modudoc.common.operation;


import java.io.Serializable;

// 문서의 내용이 변경될 때 발생하는 변경점을 저장하는 DTO 객체
// 문서의 내용을 변경할 때 내용의 추가/삭제/수정 여부와 제목/내용을 수정하는지, 어디서 어떤 길이의 내용을 바꾸는 지 등의 정보가 필요하다.
// 추후 변경 가능성 있음
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
