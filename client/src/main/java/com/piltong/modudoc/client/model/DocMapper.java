package com.piltong.modudoc.client.model;

import com.piltong.modudoc.common.model.DocumentDto;

import java.util.List;
import java.util.stream.Collectors;

public class DocMapper {
    public static DocumentDto toDto(Document doc) {
        return new DocumentDto(doc.getId(), doc.getTitle(), doc.getContent(), doc.getCreatedDate(), doc.getModifiedDate());
    }

    public static Document toEntity(DocumentDto docDto) {
        return new Document(docDto.getId(), docDto.getTitle(), docDto.getContent(), docDto.getCreatedDate(), docDto.getModifiedDate());
    }



    // DTO 리스트 -> 문서 리스트 변환
    public static List<Document> toEntity(List<DocumentDto> dtoList) {
        return dtoList
                .stream()
                .map(DocMapper::toEntity)
                .collect(Collectors.toList());
    }

}