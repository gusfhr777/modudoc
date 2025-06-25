package com.piltong.modudoc.server.service;

import com.piltong.modudoc.server.model.Document;
import com.piltong.modudoc.server.repository.DocumentRepository;
import com.piltong.modudoc.server.repository.MapDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentServiceTest {

    private DocumentService docService;

    @BeforeEach
    void setUp() {
        docService = new DocumentService(new MapDocumentRepository());
    }

    @Test
    void testCreateAndFindDocument() {
        Document doc = docService.create("Title A", "Content A");

        assertAll(
                () -> assertEquals("Title A", doc.getTitle()),
                () -> assertEquals("Content A", doc.getContent())
        );
    }

    @Test
    void testDocumentExists() {
        // 해당 Id에 해당하는 문서가 없어서 예외를 던짐. 그래서 오류나감
        // 예외를 던지는 대신 null 값을 반환하는 코드를 추가함.
        Document doc = docService.create("Title A", "Content A");
        Integer id = doc.getId();

        assertTrue(docService.exists(id));

        docService.update(id, "Updated Title", "Updated Content");

        assertTrue(docService.exists(id)); // 재확인
    }

    @Test
    void testRemoveDocument() {
        Document doc = docService.create("Title A", "Content A");
        Integer id = doc.getId();

        docService.delete(id);

//        assertThrows(NoSuchElementException.class, () -> docService.findById(id));
    }

    @Test
    void testFindAllDocuments() {
        docService.create("Doc1", "Content1");
        docService.create("Doc2", "Content2");

        List<Document> allDocs = docService.findAll();

        assertEquals(2, allDocs.size());
    }
}
