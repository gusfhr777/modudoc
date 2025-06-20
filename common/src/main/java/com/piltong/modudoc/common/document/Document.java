package com.piltong.modudoc.common.document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// 서버, 클라에서 모두 사용하는 문서 엔티티
// 문서 엔티티 : 프로그램 내부에서 사용하는 객체
public class Document{

    // 필드 정의
    private String id; // 문서의 식별자. 다른 문서와 구분하는 역할을 한다.
    private String title; // 문서 제목
    private String content; // 문서 내용
    private LocalDateTime createdDate; // 문서의 생성일자.
    private LocalDateTime modifiedDate; // 문서 최종 수정일
    private List<String> accessUserIds; // 접근하고 있는 유저아이디 리스트


    // 필드 getter 및 setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public List<String> getAccessUserIds() {
        return accessUserIds;
    }

    public void setAccessUserIds(List<String> accessUserIds) {
        this.accessUserIds = accessUserIds;
    }


    // 문서 생성자
    public Document(String id, String title, String content) {

        // 모든 데이터는 Null이 아닌 형태로 저장한다.
        this.id = Objects.requireNonNull(id);
        this.title = Objects.requireNonNull(title);
        this.content = Objects.requireNonNull(content);
        this.createdDate = LocalDateTime.now();
        this.modifiedDate = this.createdDate;
        this.accessUserIds = new ArrayList<>();
    }


    // 문서 생성자 2
    public Document(String id, String title, String content, LocalDateTime createdDate, LocalDateTime modifiedDate, List<String> accessUserIds) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.accessUserIds = accessUserIds;
    }

    // 문서 수정일 업데이트
    public void touch() {
        modifiedDate = LocalDateTime.now();
    }


    // 접근하는 유저 추가
    public void addUser(String userId) {
        if (!accessUserIds.contains(userId)) {
            accessUserIds.add(userId);
            touch();
        }
    }

    // 접근하는 유저 제거
    public void removeUser(String userId) {
        if (accessUserIds.contains(userId)) {
            accessUserIds.add(userId);
            touch();
        }
    }


    // DTO 객체 -> 엔티티 객체로 변환하는 함수
    // 추후 최적화 필요
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

    // 엔티티 객체 -> DTO 객체로 변환하는 함수
    // 추후 최적화 필요
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


    // 엔티티 객체 -> 요약 객체로 변환하는 함수
//    public static DocumentSummary toSummary(Document doc) {
//        return new DocumentSummary(
//                doc.getId(),
//                doc.getTitle(),
//                doc.getCreatedDate(),
//                doc.getModifiedDate(),
//                List.copyOf(doc.getAccessUserIds())
//        );
//    }
}
