package com.piltong.modudoc.server.service;

import com.piltong.modudoc.server.model.Document;
import com.piltong.modudoc.server.repository.DocumentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

// 문서 관련 비즈니스 로직 처리 클래스
// 문서의 생성, 저장, 수정, 삭제 등 주요 기능 제공
// 입력 유효성 검사를 포함한 예외 처리
public class DocumentService {

    // 필드
    private static final Logger log = LogManager.getLogger(DocumentService.class);
    private final DocumentRepository docRepo; // 실제 문서 데이터 저장소 역할을 하는 객체

    // 생성자: 저장소 객체 초기화
    public DocumentService(DocumentRepository documentRepository) {
        this.docRepo = documentRepository;
    }


    // 제목만 가진 문서 생성
    public Document create(String title, String content) {
        if (title == null || content == null) {
            String msg = "Invalid title or content Parameter.";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        Document doc = new Document(title, content);
        docRepo.save(doc);
        return doc;
    }



    // 문서를 획득하는 메서드
    public Document findById(Integer docId) {
        if (docId == null) {
            String msg = "Invalid docId Parameter.";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        return docRepo.findById(docId).orElseThrow(() -> {
            String msg = "Document not found. ID = " + docId;
            log.error(msg);
            return new RuntimeException(msg);
        });
    }

    // 모든 문서를 획득하는 메서드
    public List<Document> findAll() {
        return docRepo.findAll();
    }


    /**
     * [외부 입력 기반 문서 생성/수정용]
     * - 문서 ID, 제목, 내용만 받아 새 Document 객체를 생성하고 저장
     * - 이미 문서가 존재하더라도 덮어쓰는 방식
     * - 주로 사용자가 새로운 문서를 생성하거나, 전부 다시 저장할 때 사용
     */
    public void update(Integer docId, String title, String content) {
        if (docId == null || title == null || content == null) {
            String msg = "Invalid docId, title or content.";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        // 변경 사항이 있을때마다 새로운 객체를 생성 후 저장한다.
        Optional<Document> opt = docRepo.findById(docId);
        if (opt.isPresent()) {
            Document doc = opt.get();
            docRepo.save(doc);
        } else {
            String msg = "Select Failed from Document ID : " + docId.toString();
            log.error(msg);
            throw new RuntimeException(msg);
        }
    }


    public void update(Document doc) {
        if (doc.getId() == null || doc.getTitle() == null || doc.getContent() == null) {
            String msg = "Invalid docId, title or content.";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        docRepo.save(doc);
//        // 변경 사항이 있을때마다 새로운 객체를 생성 후 저장한다.
//        Optional<Document> opt = docRepo.findById(doc.getId());
//        if (opt.isPresent()) {
//            doc = opt.get();
//        } else {
//            String msg = "Select Failed from Document ID : " + doc.getId().toString();
//            log.error(msg);
//            throw new RuntimeException(msg);
//        }
    }



    // 문서 삭제 메서드
    public void delete(Integer docId) {
        if (docId == null) {
            String msg = "Invalid docId Parameter.";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        // 삭제 실패 시
        if (!docRepo.delete(docId)) {
            String msg = "Delete Failed from Document ID : " + docId.toString();
            log.error(msg);
            throw new RuntimeException(msg);
        }
    }

    // 문서의 존재 여부 반환
    public boolean exists(Integer docId) {
        return docRepo.findById(docId).isPresent();
    }

//    public void deleteDocument(Integer docId) {
//        if (docId == null) {
//            String msg = "Invalid docId Parameter.";
//            log.error(msg);
//            throw new IllegalArgumentException(msg);
//        }
//
//        if(docRepo.delete(docId)) {
//            return;
//        } else {
//            String msg = "Select Failed from Document ID : " + docId.toString();
//            log.error(msg);
//            throw new RuntimeException(msg);
//
//        }
//    }
//
//    /**
//     * [수정된 Document 객체 그대로 저장할 때 사용]
//     * - 이미 생성된 Document 객체를 받아 저장소에 저장
//     * - 문서를 가져온 후 내용만 일부 수정하는 경우에 사용
//     * - OT 적용, 자동 저장 등 내부 처리 로직에서 자주 사용됨
//     */
//    public void saveDocument(Document document) {
//        if (document == null || document.getId() == null) {
//            throw new IllegalArgumentException("문서 또는 문서 ID를 찾을 수 없음");
//        }
//        docRepo.saveDocument(document);
//    }
//
//    // 문서를 불러옴
//    public Document getDocument(String documentId) {
//        if (documentId == null) {
//            throw new IllegalArgumentException("문서 ID를 찾을 수 없음");
//        }
//        return docRepo.loadDocument(documentId);
//    }
//
//    // 문서 존재여부 판단
//    public Document findDocument(String documentId) {
//        if (documentId == null) {
//            return null;
//        }
//        return docRepo.findDocument(documentId);
//    }
//
//    // 문서를 삭제함
//    public void removeDocument(String documentId) {
//        if (documentId == null) {
//            throw new IllegalArgumentException("문서 ID를 찾을 수 없음");
//        }
//        docRepo.deleteDocument(documentId);
//    }
//
//    // 전체 문서 목록 반환
//    public List<Document> getAllDocuments() {
//        return docRepo.findAllDocuments();
//    }

}
