package com.piltong.modudoc.common.document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

//
public class DocumentSummaryDto implements Serializable {

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
    public DocumentSummaryDto(String id, String title, LocalDateTime createdDate, LocalDateTime modifiedDate, List<String> accessUserIds) {
        this.id = id;
        this.title = title;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.accessUserIds = accessUserIds;
    }
}
