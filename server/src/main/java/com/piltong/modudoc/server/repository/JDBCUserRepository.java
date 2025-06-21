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

    public JDBCUserRepository() {
        try {
            this.conn = DBManager.getConnection();
        } catch (SQLException e) {
            String msg = "JDBCUserRepository initialize failed. : DBManager getConnection() failed.";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public void save(User user) {
        if (Objects.isNull(user.getId())) { // ID가 없는 경우 -> 생성
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {
                pstmt.setString(1, user.getId());
                pstmt.setString(2, user.getUsername());
                pstmt.setString(3, user.getPassword());

                pstmt.executeUpdate();

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
