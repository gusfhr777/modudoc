package com.piltong.modudoc.server.repository;

import com.piltong.modudoc.server.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * [JDBCUserRepository]
 *
 * UserRepository 인터페이스를 구현한 클래스
 * JDBC를 통해 MySQL과 직접 연결하여 user 데이터를 조회, 생성, 수정, 삭제
 * 내부적으로 DB 연결을 유지하고 SQL 문을 통해 영속 계층과 통신
 *
 * - 사용자 생성 (INSERT)
 * - 사용자 조회 (SELECT)
 * - 사용자 수정 (UPDATE)
 * - 사용자 삭제 (DELETE)
 *
 */
public class JDBCUserRepository implements UserRepository {

    private final Connection conn;  // JDBC 연결 객체
    private static final Logger log = LogManager.getLogger(JDBCUserRepository.class);

    // SQL 상수 정의
    private static final String INSERT_SQL = "INSERT INTO users (username, password, created_date, modified_date) VALUES (?, ?, NOW(), NOW())";
    private static final String SELECT_SQL = "SELECT * FROM users WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM users WHERE id = ?";
    private static final String UPDATE_SQL = "UPDATE users SET id = ?, username = ?, password = ? WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM users";

    // 생성자: DB 연결 초기화
    public JDBCUserRepository() {
        try {
            this.conn = DBManager.getConnection();
        } catch (SQLException e) {
            log.fatal("UserRepository 초기화 실패", e);
            throw new RuntimeException("DB 연결 실패", e);
        }
    }

    /**
     * 사용자 저장 (생성 or 수정)
     * @param user 저장할 유저 객체
     * @return 저장 완료된 유저 객체
     */
    @Override
    public User save(User user) {
        if (user.getId() == null) { // 생성
            try (PreparedStatement PreState = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
                PreState.setString(1, user.getUsername());
                PreState.setString(2, user.getPassword());
                PreState.executeUpdate();

                // 자동 생성된 id 반환
                try (ResultSet rs = PreState.getGeneratedKeys()) {
                    if (rs.next())
                        user.setId(rs.getString(1));
                }

                // created_date, modified_date 포함 전체 유저 반환
                return findById(user.getId()).orElse(user); // c
            } catch (SQLException e) {
                log.error("User 생성 실패", e);
                throw new RuntimeException("User 생성 실패", e);
            }
        } else { // 업데이트
            try (PreparedStatement PreState = conn.prepareStatement(UPDATE_SQL)) {
                PreState.setString(1, user.getUsername());
                PreState.setString(2, user.getPassword());
                PreState.setString(3, user.getId());
                PreState.executeUpdate();
                return findById(user.getId()).orElse(user);
            } catch (SQLException e) {
                log.error("User 업데이트 실패", e);
                throw new RuntimeException("User 업데이트 실패", e);
            }
        }
    }

    /**
     * 사용자 단건 조회
     * @param id 유저 아이디
     * @return Optional<user>
     */
    @Override
    public Optional<User> findById(String id) {
        try (PreparedStatement PreState = conn.prepareStatement(SELECT_SQL)) {
            PreState.setString(1, id);
            try (ResultSet rs = PreState.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getString("id"),
                            rs.getString("username"),
                            rs.getString("password")
                    ));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("User 조회 실패", e);
            return Optional.empty();
        }
    }

    /**
     * 전체 사용자 목록 조회
     * @return 유저 리스트
     */
    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        try (PreparedStatement PreState = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = PreState.executeQuery()) {
            while (rs.next()) {
                list.add(new User(
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            log.error("User 전체 조회 실패", e);
        }
        return list;
    }

    /**
     * 사용자 삭제
     * @param id 삭제할 유저 아이디
     * @return 성공 여부 (true: 삭제)
     */
    @Override
    public boolean delete(String id) {
        try (PreparedStatement PteState = conn.prepareStatement(DELETE_SQL)) {
            PteState.setString(1, id);
            return PteState.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("User 삭제 실패", e);
            return false;
        }
    }
}
