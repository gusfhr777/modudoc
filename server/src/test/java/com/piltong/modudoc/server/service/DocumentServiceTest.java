package com.piltong.modudoc.server.service;

import com.piltong.modudoc.common.document.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentServiceTest {

    private DocumentService documentService;

    @BeforeEach
    void setUp() {
        documentService = new DocumentService();
    }

    @Test
    void testCreateAndGetDocument() {
        documentService.updateDocument("doc1", "Title A", "Content A");
        Document doc = documentService.getDocument("doc1");

        assertEquals("doc1", doc.getId());
        assertEquals("Title A", doc.getTitle());
        assertEquals("Content A", doc.getContent());
    }

    @Test
    void testDocumentExists() {
        assertFalse(documentService.exists("x"));
        documentService.updateDocument("y", "Y", "Y");
        assertTrue(documentService.exists("y"));
    }

    @Test
    void testRemoveDocument() {
        documentService.updateDocument("z", "Z", "Z");
        documentService.removeDocument("z");
        assertThrows(IllegalArgumentException.class, () -> documentService.getDocument("z"));
    }

    @Test
    void testGetAllDocuments() {
        documentService.updateDocument("a", "A", "A");
        documentService.updateDocument("b", "B", "B");
        List<Document> all = documentService.getAllDocuments();
        assertEquals(2, all.size());
    }
}
