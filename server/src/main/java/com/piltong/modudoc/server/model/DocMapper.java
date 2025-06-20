package com.piltong.modudoc.server.model;

import com.piltong.modudoc.common.model.DocumentDto;
import com.piltong.modudoc.server.model.Document;

import java.util.List;
import java.util.stream.Collectors;

public class DocMapper {



    // Entity -> DTO로 변환
    public static DocumentDto toDto(Document doc) {
        return new DocumentDto(doc.getId(), doc.getTitle(), doc.getContent(), doc.getCreatedDate(), doc.getModifiedDate());
    }

    // DTO -> Entity로 변환
    public static Document toEntity(DocumentDto docDto) {
        return new Document(docDto.getId(), docDto.getTitle(), docDto.getContent(), docDto.getCreatedDate(), docDto.getModifiedDate());
    }


    // docList -> dtoList로 변환
    public static List<DocumentDto> toDto(List<Document> docList) {
        return docList
                .stream()
                .map(DocMapper::toDto)
                .collect(Collectors.toList());


    }
}