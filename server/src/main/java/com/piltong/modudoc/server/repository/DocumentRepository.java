package com.piltong.modudoc.server.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.piltong.modudoc.common.document.Document;

// DB나 파일 기반 문서 저장소와 직접 연동
public class DocumentRepository {
    // Map을 사용. documentId를 키값으로 가진다.
    private final Map<String, Document> documentStorage = new HashMap<>();

    // DB 또는 파일 시스템에 저장
    public synchronized void saveDocument(Document document) {
        if (document == null || document.getId() == null) {
            throw new IllegalArgumentException("저장할 문서나 문서 ID를 찾을 수 없음");
        }
        documentStorage.put(document.getId(), document);
    }

    // 저장소에서 문서 불러오기
    public synchronized Document loadDocument(String documentId) {
        if (documentId == null) {
            throw new IllegalArgumentException("올바르지 않은 문서 아이디");
        }
        Document doc = documentStorage.get(documentId);
        if (doc == null) {
            throw new IllegalArgumentException("해당 ID의 문서를 찾을 수 없음");
        }
        return doc;
    }

    // 문서 삭제
    public synchronized void deleteDocument(String documentId) {
        if (documentId == null) {
            throw new IllegalArgumentException("문서 ID를 찾을 수 없음");
        }
        documentStorage.remove(documentId);
    }

    // 저장된 모든 문서를 리스트로 반환
    public synchronized List<Document> findAllDocuments() {
        return new ArrayList<>(documentStorage.values());
    }
}