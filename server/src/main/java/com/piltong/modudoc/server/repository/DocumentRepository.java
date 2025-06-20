package com.piltong.modudoc.server.repository;

import com.piltong.modudoc.server.model.Document;


import java.sql.SQLException;
import java.util.List;
import java.util.Optional;



// Document 객체에 대한 CRUD 기능을 제공하는 인터페이스.
// 전형적인 DAO 혹은 리포지토리 패턴에 해당한다.
public interface DocumentRepository {

    Document save(Document doc) ; // 문서 생성 및 수정(있으면 UPDATE, 없으면 INSERT). 불완전 엔티티 객체 -> 영속화된 엔티티 객체 리턴.
    Optional<Document> findById(int id) ; // 문서 단건 조회
    List<Document> findAll() ; // 문서 전체 조회
    boolean delete(int id); // 삭제, 성공 여부 반환

}
