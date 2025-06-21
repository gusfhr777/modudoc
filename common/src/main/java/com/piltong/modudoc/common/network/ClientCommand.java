package com.piltong.modudoc.common.network;

import com.piltong.modudoc.common.model.DocumentDto;
import com.piltong.modudoc.common.model.OperationDto;


/**
 * 클라이언트에서 서버로 전송 가능한 명령어 종류를 정의한 열거형입니다.
 * Server Network Handler <-> Client Network Handler 간 요청 데이터, 응답 데이터 세트가 들어있습니다.
 */
public enum ClientCommand {
    /**
     * 문서 생성 명령.
     * 요청 데이터: String (title)
     * 응답 데이터: {@link DocumentDto}
     */
    CREATE_DOCUMENT,

    /**
     * 단일 문서 조회 명령.
     * 요청 데이터: Integer (docId)
     * 응답 데이터: {@link DocumentDto}
     */
    READ_DOCUMENT,

    /**
     * 문서 수정 명령. 다른 클라에게 전파.
     * 요청 데이터: {@link DocumentDto} (updated)
     * 응답 데이터: null
     */
    UPDATE_DOCUMENT,

    /**
     * 문서 삭제 명령.
     * 요청 데이터: Integer (docId)
     * 응답 데이터: null
     */
    DELETE_DOCUMENT,

    /**
     * 문서 리스트 조회 명령.
     * 요청 데이터: null
     * 응답 데이터: {@code List<DocumentDto>}
     */
    READ_DOCUMENT_LIST,

    /**
     * OperationDto 전파 명령. 다른 클라에게 전파.
     * 요청 데이터: {@link OperationDto}
     * 응답 데이터: null
     */
    PROPAGATE_OPERATION,

    /**
     * 로그인 명령
     * 요청 데이터: loginRequestDto
     * 응답 데이터: UserDto
     */
    LOGIN;
}
