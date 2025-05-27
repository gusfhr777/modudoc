package com.piltong.modudoc.server.service;

import com.piltong.modudoc.server.repository.DocumentRepository;

// 문서 관련 비즈니스 로직 (수정/저장/가져오기 등)
public class DocumentService {
    // 실제 문서 데이터 저장소 역할을 하는 객체
    private final DocumentRepository documentRepository;

    // 생성자에서 저장소 객체 초기화
    public DocumentService() {
        this.documentRepository = new DocumentRepository();
    }

    // 문서를 업데이트/저장
    public void updateDocument(String documentId, String content) {
        documentRepository.saveDocument(documentId, content);
    }

    // 문서를 불러옴
    public String getDocument(String documentId) {
        return documentRepository.loadDocument(documentId);
    }

    // 문서를 삭제함
    public void removeDocument(String documentId) {
        documentRepository.deleteDocument(documentId);
    }
}
