package com.piltong.modudoc.client.model;

import com.piltong.modudoc.common.model.DocumentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DocMapperTest {


    @BeforeEach
    void setUp() {

        Document doc1 = new Document(1, "DocumentTest1", "ContentTest1", LocalDateTime.now(), LocalDateTime.now());
        Document doc2 = new Document(2, "DocumentTest2", "ContentTest2", LocalDateTime.now(), LocalDateTime.now());
        Document doc3 = new Document(3, "DocumentTest3", "ContentTest3", LocalDateTime.now(), LocalDateTime.now());
        Document doc4 = new Document(4, "DocumentTest4", "ContentTest4", LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void testToDto() {
        Document doc = new Document(1, "Title", "Content",
                LocalDateTime.of(2023, 1, 1, 0, 0),
                LocalDateTime.of(2023, 1, 2, 0, 0));

        DocumentDto dto = DocMapper.toDto(doc);

        assertEquals(doc.getId(), dto.getId());
        assertEquals(doc.getTitle(), dto.getTitle());
        assertEquals(doc.getContent(), dto.getContent());
        assertEquals(doc.getCreatedDate(), dto.getCreatedDate());
        assertEquals(doc.getModifiedDate(), dto.getModifiedDate());

    }

    @Test
    void testToEntity() {
        DocumentDto dto = new DocumentDto(2, "Another", "Body",
                LocalDateTime.of(2023, 2, 1, 0, 0),
                LocalDateTime.of(2023, 2, 2, 0, 0));

        Document doc = DocMapper.toEntity(dto);

        assertEquals(dto.getId(), doc.getId());
        assertEquals(dto.getTitle(), doc.getTitle());
        assertEquals(dto.getContent(), doc.getContent());
        assertEquals(dto.getCreatedDate(), doc.getCreatedDate());
        assertEquals(dto.getModifiedDate(), doc.getModifiedDate());

    }

    @Test
    void testToEntityList() {
        List<DocumentDto> dtos = List.of(
                new DocumentDto(1, "T1", "C1", LocalDateTime.now(), LocalDateTime.now()),
                new DocumentDto(2, "T2", "C2", LocalDateTime.now(), LocalDateTime.now())
        );

        List<Document> docs = DocMapper.toEntity(dtos);

        assertEquals(docs.size(), dtos.size());
        for (int i = 0; i < docs.size(); i++) {
            assertEquals(docs.get(i).getId(), dtos.get(i).getId());
            assertEquals(docs.get(i).getTitle(), dtos.get(i).getTitle());
        }



    }

}