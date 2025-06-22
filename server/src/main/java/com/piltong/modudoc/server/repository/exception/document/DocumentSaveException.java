package com.piltong.modudoc.server.repository.exception.document;

// 문서 Repository 저장 예외
public class DocumentSaveException extends DocumentRepositoryException {
    public DocumentSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
