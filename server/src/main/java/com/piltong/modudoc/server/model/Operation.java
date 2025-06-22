package com.piltong.modudoc.server.model;


import com.piltong.modudoc.common.model.OperationType;

// 문서의 내용이 변경될 때 발생하는 변경점을 저장하는 객체
// 문서의 내용을 변경할 때 내용의 추가/삭제/수정 여부와 제목/내용을 수정하는지, 어디서 어떤 길이의 내용을 바꾸는 지 등의 정보가 필요하다.
public class Operation {

    private OperationType operationType; // 삽입, 삭제 등 연산 종류
    private int docId;                   // 대상 문서 ID
    private int position;                // 연산 위치
    private String content;              // 삽입/삭제할 내용


    // Getter 및 Setter
    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    // 생성자
    public Operation(OperationType operationType, int docId, int position, String content) {
        this.operationType = operationType;
        this.docId = docId;
        this.position = position;
        this.content = content;
    }


    @Override
    public String toString() {
        return "Operation{" +
                "operationType=" + operationType +
                ", docId=" + docId +
                ", position=" + position +
                ", content='" + content + '\'' +
                '}';
    }



}
