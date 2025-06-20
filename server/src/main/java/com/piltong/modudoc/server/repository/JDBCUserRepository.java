package com.piltong.modudoc.server.repository;

import com.piltong.modudoc.server.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class JDBCUserRepository implements UserRepository {

    private final Connection conn;
    private static final Logger log = LogManager.getLogger(JDBCUserRepository.class);

    private static final String INSERT_SQL = "INSERT INTO users (username, password, created_date, modified_date) VALUES (?, ?, NOW(), NOW())";
    private static final String SELECT_SQL = "SELECT * FROM users WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM users WHERE id = ?";
    private static final String UPDATE_SQL = "UPDATE users SET username = ?, password = ?, modified_date = NOW() WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM users";

    public JDBCUserRepository() {
        try {
            this.conn = DBManager.getConnection();
        } catch (SQLException e) {
            log.fatal("UserRepository 초기화 실패", e);
            throw new RuntimeException("DB 연결 실패", e);
        }
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) { // 생성
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getPassword());
                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next())
                        user.setId(rs.getString(1)); // 보통 AUTO_INCREMENT인 경우
                }

                return findById(user.getId()).orElse(user); // created_date 채워서 리턴
            } catch (SQLException e) {
                log.error("User 생성 실패", e);
                throw new RuntimeException("User 생성 실패", e);
            }
        } else { // 업데이트
            try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getId());
                pstmt.executeUpdate();
                return findById(user.getId()).orElse(user);
            } catch (SQLException e) {
                log.error("User 업데이트 실패", e);
                throw new RuntimeException("User 업데이트 실패", e);
            }
        }
    }

    @Override
    public Optional<User> findById(String id) {
        try (PreparedStatement pstmt = conn.prepareStatement(SELECT_SQL)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getString("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getTimestamp("created_date").toLocalDateTime(),
                            rs.getTimestamp("modified_date").toLocalDateTime()
                    ));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("User 조회 실패", e);
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(new User(
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getTimestamp("created_date").toLocalDateTime(),
                        rs.getTimestamp("modified_date").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            log.error("User 전체 조회 실패", e);
        }
        return list;
    }

    @Override
    public boolean delete(String id) {
        try (PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {
            pstmt.setString(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("User 삭제 실패", e);
            return false;
        }
    }
}
