package com.piltong.modudoc.common.document;


import java.util.List;

// 여러 문서를 저장하는 문서 리스트 엔티티.
// Document는 하나의 문서에 대한 객체이고, DocumentList는 그 문서 객체들의 모음이다.
// common에서 정의를 하였으나, 클라측에서 사용할 일은 없다.
public class DocumentList {

    // 필드 선언
    private List<Document> items; // 여러 문서를 가질 수 있는 리스트형 객체.


    // Getter 및 Setter
    public List<Document> getDocumentList() {
        return items;
    }

    public void setDocumentList(List<Document> documentList) {
        this.items = documentList;
    }

}
