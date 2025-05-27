package com.piltong.modudoc.common.network;


// 클라이언트에서 서버에게 실행가능한 명령어 모음.
// 클라 -> 서버 -> 클라로의 요청이 이루어진다.
public enum ClientCommand {
    CREATE_DOCUMENT, // 문서를 생성한다. DocumentSummary created
    READ_DOCUMENT, // 문서를 읽는다.
    UPDATE_DOCUMENT, // 문서를 업데이트한다.
    DELETE_DOCUMENT, // 문서를 삭제한다.
    READ_DOCUMENT_SUMMARIES, // 문서 요약 리스트 객체를 읽는다.
    PROPAGATE_OPERATION, // Operation을 서버와 다른 클라이언트에 전파한다. 클라A -> 서버 -> 클라B(전파), 클라A(성공여부)로 데이터가 전달된다.
}