package com.piltong.modudoc.common.document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

// 서버, 클라에서 통신을 하기 위한 목적을 가진 문서 DTO(Data Transfer Object) 클래스
// 문서 DTO : 통신용으로 전달하기 위한 문서 객체
public class DocumentDto implements Serializable {

    // 필드 정의
    private final Integer id; // 문서의 식별자. 다른 문서와 구분하는 역할을 한다.
    private final String title; // 문서 제목
    private final String content; // 문서 내용
    private final LocalDateTime createdDate; // 문서의 생성일자.
    private final LocalDateTime modifiedDate; // 문서 최종 수정일
    private final List<String> accessUserIds; // 접근하고 있는 유저아이디 리스트


    // Getter
    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public List<String> getAccessUserIds() {
        return accessUserIds;
    }


    // 문서DTO 생성자
    public DocumentDto(Integer id, String title, String content, LocalDateTime createdDate, LocalDateTime modifiedDate, List<String> accessUserIds) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.accessUserIds = accessUserIds;
    }


}
