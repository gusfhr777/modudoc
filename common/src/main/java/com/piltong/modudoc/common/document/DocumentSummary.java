package com.piltong.modudoc.common.document;


import java.time.LocalDateTime;
import java.util.List;

// 문서 요약 클래스. 클라이언트에서 문서 목록을 보여주기 위한 메타데이터만을 가진다.
public class DocumentSummary {

    // 필드 정의
    private String id; // 문서의 식별자. 다른 문서와 구분하는 역할을 한다.
    private String title; // 문서 제목
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


    // 문서 요약 생성자.
    public DocumentSummary(String id, String title, LocalDateTime createdDate, LocalDateTime modifiedDate, List<String> accessUserIds) {
        this.id = id;
        this.title = title;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.accessUserIds = accessUserIds;
    }


    // DTO 객체 -> 엔티티 객체로 변환하는 함수
    // 추후 최적화 필요
    public static DocumentSummary toEntity(DocumentSummaryDto docSummary) {
        return new DocumentSummary(
                docSummary.getId(),
                docSummary.getTitle(),
                docSummary.getCreatedDate(),
                docSummary.getModifiedDate(),
                List.copyOf(docSummary.getAccessUserIds())
        );
    }

    // 엔티티 객체 -> DTO 객체로 변환하는 함수
    // 추후 최적화 필요
    public static DocumentSummaryDto toDto(DocumentSummary docSummary) {
        return new DocumentSummaryDto(
                docSummary.getId(),
                docSummary.getTitle(),
                docSummary.getCreatedDate(),
                docSummary.getModifiedDate(),
                List.copyOf(docSummary.getAccessUserIds())
        );
    }
}
