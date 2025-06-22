package com.piltong.modudoc.server.repository.exception.document;

// 문서 Repository 조회 예외
public class DocumentReadException extends DocumentRepositoryException {
    public DocumentReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
