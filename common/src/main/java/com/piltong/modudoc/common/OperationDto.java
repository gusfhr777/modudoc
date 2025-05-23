package com.piltong.modudoc.common;


// 오퍼레이션 타입이 저장되는 열거형.
enum OperationType {
    INSERT, // 데이터 추가
    DELETE, // 데이터 삭제
    MODIFY, // 데이터 수정
    FORMATTING // 데이터 포매팅 - (사용 가능성 미지수)
}

// 문서에서 변경할 필드
enum EditPosition {
    TITLE, // 문서 제목
    CONTENT, // 문서 내용
}

// 문서의 내용이 변경될 때 발생하는 변경점을 저장하는 객체
// 문서의 내용을 변경할 때 내용의 추가/삭제/수정 여부와 제목/내용을 수정하는지, 어디서 어떤 길이의 내용을 바꾸는 지 등의 정보가 필요하다.
// 추후 변경 가능성 있음
public class OperationDto {
    private OperationType operationType; // 문서 형태
    private EditPosition editPosition; // 변경할려는 객체

    private int position; // 변경 위치
    private int length; // 변경할 길이
    private String content; // 변경 내용
}
