package com.piltong.modudoc.server.service;

import com.piltong.modudoc.server.model.Document;
import com.piltong.modudoc.server.repository.DocumentRepository;
import com.piltong.modudoc.server.repository.exception.document.DocumentReadException;
import com.piltong.modudoc.server.repository.exception.document.DocumentSaveException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.NoSuchElementException;
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
        log.info("DocumentService initialized");
    }


    // 제목만 가진 문서 생성
    public Document create(String title, String content) {
        log.info("Document create");
        if (title == null || content == null) {
            String errMsg = "Invalid title or content Parameter.";
            log.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Document doc = new Document(title, content);
        try {
            docRepo.save(doc);
        } catch (DocumentSaveException e) {
            String errMsg = "document create Failed.";
            log.error(errMsg);
        }

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
        try {
            return docRepo.findAll();
        } catch (DocumentReadException e) { // 읽기 실패
            String errMsg = "document read failed.";
            throw e;
        }
    }


    /**
     * [외부 입력 기반 문서 생성/수정용]
     * - 문서 ID, 제목, 내용만 받아 새 Document 객체를 생성하고 저장
     * - 이미 문서가 존재하더라도 덮어쓰는 방식
     * - 주로 사용자가 새로운 문서를 생성하거나, 전부 다시 저장할 때 사용
     */
    public void update(Integer docId, String title, String content) {
        log.info("Document update : {} : {} : {}", docId, title, content);
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
        log.info("Document update : " + doc);
        if (doc.getId() == null || doc.getTitle() == null || doc.getContent() == null) {
            String msg = "Invalid docId, title or content.";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        docRepo.save(doc);
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
            log.info("Document delete failed : {}", docId);
            String msg = "Delete Failed from Document ID : " + docId.toString();
            log.error(msg);
            throw new RuntimeException(msg);
        }
        log.info("Document delete success : {}", docId);
    }

    // 문서의 존재 여부 반환
    public boolean exists(Integer docId) {
        boolean exists = docRepo.findById(docId).isPresent();
        log.info("Document exists : {}", exists);
        return exists;
    }


}
