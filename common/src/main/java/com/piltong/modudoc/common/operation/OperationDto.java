package com.piltong.modudoc.common.operation;


import java.io.Serializable;

// 문서의 내용이 변경될 때 발생하는 변경점을 저장하는 DTO 객체
// 문서의 내용을 변경할 때 내용의 추가/삭제/수정 여부와 제목/내용을 수정하는지, 어디서 어떤 길이의 내용을 바꾸는 지 등의 정보가 필요하다.
// 추후 변경 가능성 있음
public class OperationDto implements Serializable {

    private OperationType operationType; // 문서 형태
    private EditPosition editPosition; // 변경할려는 객체

    private int position; // 변경 위치
    private String content; // 변경 내용


    // 생성자
    public OperationDto(OperationType operationType, EditPosition editPosition, int position, String content) {
        this.operationType = operationType;
        this.editPosition = editPosition;
        this.position = position;
        this.content = content;
    }

    // Getter
    public OperationType getOperationType() {
        return operationType;
    }

    public EditPosition getEditPosition() {
        return editPosition;
    }

    public int getPosition() {
        return position;
    }

    public String getContent() {
        return content;
    }



}
