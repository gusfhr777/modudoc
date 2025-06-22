package com.piltong.modudoc.server.repository.exception.document;

import com.piltong.modudoc.server.repository.exception.RepositoryException;

// 문서 Repository 관련 예외
public class DocumentRepositoryException extends RepositoryException {
    public DocumentRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
