package com.piltong.modudoc.server.repository.exception.document;

// 문서 Repository 초기화 예외
public class DocumentRepoInitException extends DocumentRepositoryException {
    public DocumentRepoInitException(String message, Throwable cause) {
        super(message, cause);
    }
}
