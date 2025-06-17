package com.piltong.modudoc.common.operation;



// 문서의 내용이 변경될 때 발생하는 변경점을 저장하는 객체
// 문서의 내용을 변경할 때 내용의 추가/삭제/수정 여부와 제목/내용을 수정하는지, 어디서 어떤 길이의 내용을 바꾸는 지 등의 정보가 필요하다.
public class Operation {

    private OperationType operationType; // 문서 형태

    private int position; // 변경 위치


    private String content; // 변경 내용


    // 생성자
    public Operation(OperationType operationType, int position, String content) {
        this.operationType = operationType;
        this.position = position;
        this.content = content;
    }

    // Getter 및 Setter
    public OperationType getOperationType() {
        return operationType;
    }


    public int getPosition() {
        return position;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }


    // DTO 객체로 변환하는 함수
    // 추후 최적화 필요
    public static OperationDto toDto(Operation operation) {

        return new OperationDto(
                operation.operationType,
                operation.editPosition,
                operation.position,
                operation.content
        );
    }

    // 엔티티 객체로 변환하는 함수
    // 추후 최적화 필요
    public static Operation toEntity(OperationDto operationDto) {

        return new Operation(
                operationDto.getOperationType(),
                operationDto.getEditPosition(),
                operationDto.getPosition(),
                operationDto.getContent()
        );
    }

}
