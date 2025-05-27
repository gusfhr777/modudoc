package com.piltong.modudoc.server.repository;

import java.util.HashMap;
import java.util.Map;
import com.piltong.modudoc.common.document.Document;

// DB나 파일 기반 문서 저장소와 직접 연동
public class DocumentRepository {
    // Map을 사용. documentId를 키값으로 가진다.
    private final Map<String, Document> documentStorage = new HashMap<>();

    // DB 또는 파일 시스템에 저장
    public synchronized void saveDocument(Document document) {
        documentStorage.put(document.getId(), document);
    }

    // 저장소에서 문서 불러오기
    public synchronized Document loadDocument(String documentId) {
        return documentStorage.get(documentId);
    }

    // 문서 삭제
    public synchronized void deleteDocument(String documentId) {
        documentStorage.remove(documentId);
    }
}