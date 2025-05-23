package com.piltong.modudoc.common;


import java.util.List;

// 문서 엔티티 <-> DTO 변환 클래스
// 문서 엔티티 : 프로그램 내부에서 사용하는 객체
// 문서 DTO : 통신용으로 전달하기 위한 객체
public class DocumentMapper {


    // 문서 엔티티 -> DTO 변환
    public static DocumentDto toDto(Document doc) {
        return new DocumentDto(
                doc.getId(),
                doc.getTitle(),
                doc.getContent(),
                doc.getCreatedDate(),
                doc.getModifiedDate(),
                List.copyOf(doc.getAccessUserIds())
        );
    }


    // 문서 DTO -> 엔티티 변환
    public static Document toEntity(DocumentDto doc) {
        return new Document(
                doc.getId(),
                doc.getTitle(),
                doc.getContent(),
                doc.getCreatedDate(),
                doc.getModifiedDate(),
                List.copyOf(doc.getAccessUserIds())
        );
    }
}
