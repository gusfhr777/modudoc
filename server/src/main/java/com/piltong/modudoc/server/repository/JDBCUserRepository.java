package com.piltong.modudoc.server.repository;

import com.piltong.modudoc.server.model.Document;
import com.piltong.modudoc.server.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// User 객체에 대한 CRUD 기능을 제공하는 인터페이스.
// 전형적인 DAO 혹은 리포지토리 패턴에 해당한다.
public class JDBCUserRepository implements UserRepository {

    // 필드
    private final Connection conn;
    private static final Logger log = LogManager.getLogger(JDBCDocumentRepository.class);
    private static final String INSERT_SQL = "INSERT INTO users (id, username, password) VALUES (?, ?, ?)";
    private static final String SELECT_SQL = "SELECT * FROM users WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM users WHERE id = ?";
    private static final String UPDATE_SQL = "UPDATE users SET id = ?, username = ?, password = ? WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM users";

    // 생성자: DB 연결 초기화
    public JDBCUserRepository() {
        try {
            this.conn = DBManager.getConnection();
        } catch (SQLException e) {
            String msg = "JDBCUserRepository initialize failed. : DBManager getConnection() failed.";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    /**
     * 사용자 저장 (생성 or 수정)
     * @param user 저장할 유저 객체
     * @return 저장 완료된 유저 객체
     */
    @Override
    public void save(User user) {
        if (Objects.isNull(user.getId())) { // ID가 없는 경우 -> 생성
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {
                pstmt.setString(1, user.getId());
                pstmt.setString(2, user.getUsername());
                pstmt.setString(3, user.getPassword());

                pstmt.executeUpdate();

                // created_date, modified_date 포함 전체 유저 반환
            } catch (SQLException e) {
                String msg = "Document Create Failed.";
                log.error(msg, e);
                throw new RuntimeException(msg, e);
            }


        } else { // ID가 있는 경우 -> 업데이트
            try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)){
                pstmt.setString(1, user.getId());
                pstmt.setString(2, user.getUsername());
                pstmt.setString(3, user.getPassword());

                pstmt.executeUpdate();

            } catch (SQLException e) {
                String msg = "Document Update Failed.";
                log.error(msg, e);
                throw new RuntimeException(msg, e);
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

        try (PreparedStatement pstmt = conn.prepareStatement(SELECT_SQL)){
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(rs.getString(1), rs.getString(2), rs.getString(3)));

                }
            } catch (SQLException e) {
                return Optional.empty();
            }
        } catch (SQLException e) {
            String msg = "Document Select Failed.";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
        return null;
    }

    /**
     * 전체 사용자 목록 조회
     * @return 유저 리스트
     */
    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<User>();

        try (PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_SQL)){
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new User(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3)));
                }
            }
            return list;
        } catch (SQLException e) {
            String msg = "Document Select Failed.";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    /**
     * 사용자 삭제
     * @param id 삭제할 유저 아이디
     * @return 성공 여부 (true: 삭제)
     */
    @Override
    public boolean delete(String id) {
        try (PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)){
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            String msg = "Document Select Failed.";
            log.error(msg, e);
            return false;
        }
    }

}
