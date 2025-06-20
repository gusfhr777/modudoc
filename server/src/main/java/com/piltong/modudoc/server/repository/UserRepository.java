package com.piltong.modudoc.server.repository;

import com.piltong.modudoc.server.model.User;

import java.util.List;
import java.util.Optional;


// User 객체에 대한 CRUD 기능을 제공하는 인터페이스.
// 전형적인 DAO 혹은 리포지토리 패턴에 해당한다.
public interface UserRepository {

    /**
     * 유저를 생성 및 수정하는 메서드. 영속화된 엔티티 객체를 리턴한다.
     * 유저를 생성하는 경우 id, created_date, modified_date 필드 없이 전달한다.(불완전 객체) 이후 리턴하는 유저는 이 필드값이 채워진 채로 반환한다.
     * 유저 생성과 업데이트 모두 파라미터의 객체를 그대로 출력한다.
     * @param user : 전달하는 유저 객체. 생성 시, title, content 이외 값이 없다.
     * @return User : 생성, 업데이트 결과로 반환하는 객체.
     */
    User save(User user) ;

    /**
     * 유저 단건을 조회한다.
     * @param id : 유저 아이디
     * @return Optional<User> : Optional 객체를 리턴하여 Null을 안전하게 처리한다.
     */
    Optional<User> findById(String id) ;

    /**
     * 유저 전체를 조회한다.
     * @return : 유저 리스트를 리턴한다.
     */
    List<User> findAll();

    /**
     * 유저를 삭제한다.
     * @param id : 문 아이디
     * @return : 유저의 삭제 성공 여부를 반환한다.
     */
    boolean delete(String id);

}
