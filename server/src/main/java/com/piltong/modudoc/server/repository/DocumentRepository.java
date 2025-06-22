package com.piltong.modudoc.server.repository;

import com.piltong.modudoc.server.model.Document;


import java.sql.SQLException;
import java.util.List;
import java.util.Optional;



// Document 객체에 대한 CRUD 기능을 제공하는 인터페이스.
// 전형적인 DAO 혹은 리포지토리 패턴에 해당한다.
public interface DocumentRepository {

    /**
     * 문서를 생성 및 수정하는 메서드. 영속화된 엔티티 객체를 리턴한다.
     * 문서를 생성하는 경우 id, created_date, modified_date 필드 없이 전달한다.(불완전 객체) 이후 리턴하는 문서는 이 필드값이 채워진 채로 반환한다.
     * 문서 생성과 업데이트 모두 파라미터의 객체를 그대로 출력한다.
     * @param doc : 전달하는 문서 객체. 생성 시, title, content 이외 값이 없다.
     * @return Document : 생성, 업데이트 결과로 반환하는 객체.
     */
    Document save(Document doc) ;

    /**
     * 문서 단건을 조회한다.
     * @param id : 문서 아이디
     * @return Optional<Document> : Optional 객체를 리턴하여 Null을 안전하게 처리한다.
     */
    Optional<Document> findById(Integer id) ;

    /**
     * 문서 전체를 조회한다.
     * @return : 문서 리스트를 리턴한다.
     */
    List<Document> findAll();

    /**
     * 문서를 삭제한다.
     * @param id : 문서 아이디
     * @return : 문서의 삭제 성공 여부를 반환한다.
     */
    boolean delete(Integer id);

}
