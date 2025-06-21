package com.piltong.modudoc.common.model;

import java.io.Serializable;import java.time.LocalDateTime;

public class DashDocDto implements Serializable{
    private Integer id; // 문서 아이디
    private String title; // 문서 제목
    private LocalDateTime createdDate; // 생성 일자
    private LocalDateTime modifiedDate; // 수정 일자
    private int activeUserCount; // 접속 중인 유저 수


    @Override
    public String toString() {
        return "DashDocDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", activeUserCount=" + activeUserCount +
                '}';
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public void setActiveUserCount(int activeUserCount) {
        this.activeUserCount = activeUserCount;
    }

    public DashDocDto(Integer id, String title, LocalDateTime createdDate, LocalDateTime modifiedDate, int activeUserCount) {
        this.id = id;
        this.title = title;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.activeUserCount = activeUserCount;
    }

    public int getActiveUserCount() {
        return activeUserCount;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public String getTitle() {
        return title;
    }

    public Integer getId() {
        return id;
    }


}