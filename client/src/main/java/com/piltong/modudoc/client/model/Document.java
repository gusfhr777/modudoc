package com.piltong.modudoc.client.model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// 클라이언트 상태 관리를 위한 문서 객체이다.
// 서버에서 받은 데이터를 표시하거나, 로컬 편집 상태를 추적한다.
// 뷰 연동을 위한 UI 관련 필드를 포함한다.
public class Document {

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
}
