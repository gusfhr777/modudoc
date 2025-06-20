package com.piltong.modudoc.common.network;

import com.piltong.modudoc.common.document.Document;
import com.piltong.modudoc.common.operation.Operation;


/**
 * 클라이언트에서 서버로 전송 가능한 명령어 종류를 정의한 열거형입니다.
 */
public enum ClientCommand {
    /**
     * 문서 생성 명령.
     * 요청 데이터: String (docTitle)
     * 응답 데이터: {@link Document}
     */
    CREATE_DOCUMENT,

    /**
     * 단일 문서 조회 명령.
     * 요청 데이터: String (readId)
     * 응답 데이터: {@link Document}
     */
    READ_DOCUMENT,

    /**
     * 문서 수정 명령.
     * 요청 데이터: {@link Document} (updated)
     * 응답 데이터: null
     */
    UPDATE_DOCUMENT,

    /**
     * 문서 삭제 명령.
     * 요청 데이터: String (deleteId)
     * 응답 데이터: null
     */
    DELETE_DOCUMENT,

    /**
     * 문서 요약 리스트 조회 명령.
     * 요청 데이터: null
     * 응답 데이터: {@code List<Document>}
     */
    READ_DOCUMENT_SUMMARIES,

    /**
     * Operation 전파 명령.
     * 요청 데이터: {@link Operation}
     * 응답 데이터: null
     */
    PROPAGATE_OPERATION;
}
