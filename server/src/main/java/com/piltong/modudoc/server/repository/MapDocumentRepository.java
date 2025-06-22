package com.piltong.modudoc.server.repository;

import com.piltong.modudoc.server.model.Document;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.*;

// DB나 파일 기반 문서 저장소와 직접 연동
public class MapDocumentRepository implements DocumentRepository {
    private static final Logger log = LogManager.getLogger(MapDocumentRepository.class);
    // Map을 사용. documentId를 키값으로 가진다.
    private final Map<Integer, Document> documentStorage = new HashMap<>();


    // DB 또는 파일 시스템에 저장
    public synchronized Document save(Document document) {
        // 파라미터 검사
        if (document == null) { // 파라미터 검사
            String errMsg = "document is null.";
            throw new IllegalArgumentException(errMsg);
        }

        // 값 업데이트
        document.setModifiedDate(LocalDateTime.now()); // 수정일 업데이트
        if (document.getTitle() == null) {
            document.setTitle("");
        }
        if (document.getContent() == null) {
            document.setContent("");
        }
        if (document.getCreatedDate() == null) {
            document.setCreatedDate(LocalDateTime.now());
        }
        if (document.getId() == null) {
            document.setId(documentStorage.size()+1);
        }

        if (!documentStorage.containsKey(document.getId())) { // 아이디 여부에 따라 생성, 수정을 분기한다.
            documentStorage.put(document.getId(), document);
        } else { // 수정 작업
            documentStorage.replace(document.getId(), document);
        }

        return document;
    }

    // 저장소에서 문서 불러오기
    public synchronized Optional<Document> findById(Integer documentId) {

        if (documentId == null) {
            String errMsg = "Illegal Argument : document id is null.";
            throw new IllegalArgumentException(errMsg);
        }

        Optional<Document> doc;
        if (documentStorage.containsKey(documentId)) {
            doc = Optional.of(documentStorage.get(documentId));
        } else {
            doc = Optional.empty();
        }

        return doc;
    }

    // 문서 삭제
    public synchronized boolean delete(Integer documentId) {
        if (documentId == null) {
            String errMsg = "Illegal Argument : document id is null.";
            log.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        if (documentStorage.containsKey(documentId)) {
            documentStorage.remove(documentId);
            return true;
        } else {
            return false;
        }
    }

    // 저장된 모든 문서를 리스트로 반환
    public synchronized List<Document> findAll() {
        return new ArrayList<>(documentStorage.values());
    }
}