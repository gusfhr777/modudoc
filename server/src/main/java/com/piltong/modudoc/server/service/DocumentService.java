package com.piltong.modudoc.server.service;

import com.piltong.modudoc.server.model.Document;
import com.piltong.modudoc.server.repository.DocumentRepository;

import java.util.List;

// 문서 관련 비즈니스 로직 처리 클래스
// 문서의 생성, 저장, 수정, 삭제 등 주요 기능 제공
// 입력 유효성 검사를 포함한 예외 처리
public class DocumentService {
    // 실제 문서 데이터 저장소 역할을 하는 객체
    private final DocumentRepository documentRepository;

    // 생성자: 저장소 객체 초기화
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    /**
     * [외부 입력 기반 문서 생성/수정용]
     * - 문서 ID, 제목, 내용만 받아 새 Document 객체를 생성하고 저장
     * - 이미 문서가 존재하더라도 덮어쓰는 방식
     * - 주로 사용자가 새로운 문서를 생성하거나, 전부 다시 저장할 때 사용
     */
    public void updateDocument(String documentId, String title, String content) {
        if (documentId == null || title == null || content == null) {
            throw new IllegalArgumentException("문서 ID, 제목, 내용의 값을 찾을 수 없음");
        }
        // 변경 사항이 있을때마다 새로운 객체를 생성 후 저장한다.
        Document document = new Document(documentId, title, content);
        documentRepository.saveDocument(document);
    }

    /**
     * [수정된 Document 객체 그대로 저장할 때 사용]
     * - 이미 생성된 Document 객체를 받아 저장소에 저장
     * - 문서를 가져온 후 내용만 일부 수정하는 경우에 사용
     * - OT 적용, 자동 저장 등 내부 처리 로직에서 자주 사용됨
     */
    public void saveDocument(Document document) {
        if (document == null || document.getId() == null) {
            throw new IllegalArgumentException("문서 또는 문서 ID를 찾을 수 없음");
        }
        documentRepository.saveDocument(document);
    }

    // 문서를 불러옴
    public Document getDocument(String documentId) {
        if (documentId == null) {
            throw new IllegalArgumentException("문서 ID를 찾을 수 없음");
        }
        return documentRepository.loadDocument(documentId);
    }

    // 문서를 삭제함
    public void removeDocument(String documentId) {
        if (documentId == null) {
            throw new IllegalArgumentException("문서 ID를 찾을 수 없음");
        }
        documentRepository.deleteDocument(documentId);
    }

    // 전체 문서 목록 반환
    public List<Document> getAllDocuments() {
        return documentRepository.findAllDocuments();
    }

    // 문서의 존재 여부 반환
    public boolean exists(String documentId) {
        return getDocument(documentId) != null;
    }
}
