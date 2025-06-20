package com.piltong.modudoc.server.repository;

import com.piltong.modudoc.server.model.Document;

import java.util.*;

// DB나 파일 기반 문서 저장소와 직접 연동
public class MapDocumentRepository implements DocumentRepository {
    // Map을 사용. documentId를 키값으로 가진다.
    private final Map<Integer, Document> documentStorage = new HashMap<>();

    // DB 또는 파일 시스템에 저장
    public synchronized Document save(Document document) {
        if (document == null || document.getId() == null) {
            throw new IllegalArgumentException("저장할 문서나 문서 ID를 찾을 수 없음");
        }
        documentStorage.put(document.getId(), document);
        return document;
    }

    // 저장소에서 문서 불러오기
    public synchronized Optional<Document> findById(int documentId) {
        Optional<Document> doc = Optional.ofNullable(documentStorage.get(documentId));
        if (doc == null) {
            throw new IllegalArgumentException("해당 ID의 문서를 찾을 수 없음");
        }
        return doc;
    }

    // 문서 삭제
    public synchronized boolean delete(int documentId) {
        documentStorage.remove(documentId);
        return true;
    }

    // 저장된 모든 문서를 리스트로 반환
    public synchronized List<Document> findAll() {
        return new ArrayList<>(documentStorage.values());
    }
}