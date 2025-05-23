package com.piltong.modudoc.server;

import java.util.HashMap;
import java.util.Map;

// DB나 파일 기반 문서 저장소와 직접 연동
public class DocumentRepository {
    private final Map<String, String> documentStorage = new HashMap<>();

    // DB 또는 파일 시스템에 저장
    public synchronized void saveDocument(String documentId, String content) {
        documentStorage.put(documentId, content);
    }

    // 저장소에서 문서 불러오기
    public synchronized String loadDocument(String documentId) {
        return documentStorage.getOrDefault(documentId, "");
    }

    // 문서 삭제
    public synchronized void deleteDocument(String documentId) {
        documentStorage.remove(documentId);
    }
}