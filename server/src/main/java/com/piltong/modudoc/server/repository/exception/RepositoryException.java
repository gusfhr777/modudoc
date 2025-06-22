package com.piltong.modudoc.server.repository.exception;

// Repository에서 로직 처리 중 발생한 모든 예외의 상위 클래스.
public abstract class RepositoryException extends RuntimeException {
    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

}
