package com.piltong.modudoc.server.repository.exception.document;

// 문서 Repository 삭제 예외
public class DocumentDeleteException extends DocumentRepositoryException {
    public DocumentDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
