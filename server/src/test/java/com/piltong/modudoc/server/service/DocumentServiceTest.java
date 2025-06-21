package com.piltong.modudoc.server.service;

import com.piltong.modudoc.server.model.Document;
import com.piltong.modudoc.server.repository.DocumentRepository;
import com.piltong.modudoc.server.repository.MapDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentServiceTest {

    private DocumentService docService;

    @BeforeEach
    void setUp() {
        DocumentRepository docRepo = new MapDocumentRepository();

        docRepo.save(new Document(1, "DocumentTest1", "ContentTest1", LocalDateTime.now(), LocalDateTime.now()));
        docRepo.save(new Document(2, "DocumentTest2", "ContentTest2", LocalDateTime.now(), LocalDateTime.now()));
        docRepo.save(new Document(2, "DocumentTest3", "ContentTest3", LocalDateTime.now(), LocalDateTime.now()));

        docService = new DocumentService(docRepo);
    }

    @Test
    void testCreateAndFindDocument() {
        Document doc = docService.create("Title A", "Content A");

        assertEquals("Title A", doc.getTitle());
        assertEquals("Content A", doc.getContent());
    }

    @Test
    void testDocumentExists() {
        // 해당 Id에 해당하는 문서가 없어서 예외를 던짐. 그래서 오류나감
        // 예외를 던지는 대신 null 값을 반환하는 코드를 추가함.
        Document doc1 = docService.create("Title A", "Content A");

        Integer docId1 = doc1.getId();

        assertTrue(docService.exists(docId1));
        docService.update(docId1, "Y", "Y");
        assertTrue(docService.exists(docId1));
    }

    @Test
    void testRemoveDocument() {
        Document doc = docService.create("Title A", "Content A");
        docService.update(doc.getId(), "Z", "Z");
        docService.delete(doc.getId());
        assertThrows(IllegalArgumentException.class, () -> docService.findById(32838));
    }

    @Test
    void testFindAllDocuments() {
        docService.create("A", "B");
        docService.create("B", "B");
        List<Document> all = docService.findAll();
        assertEquals(2, all.size());
    }
}
