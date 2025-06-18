package com.piltong.modudoc.server.repository;

import java.sql.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.piltong.modudoc.server.util.Constants;
import com.piltong.modudoc.server.model.Document;
//import com.piltong.modudoc.common.RichTextSerializer;
import com.piltong.modudoc.server.model.User;

import org.fxmisc.richtext.model.StyledDocument;



// 데이터베이스를 관리하는 클래스. MySQL Database <-> Server의 관리를 중개한다.
// 서비스 로직에서 사용하는 클래스를 정의한다. Domain 객체에 대한 CRUD를 지원한다.
public class DBManager {
    public static Logger log = LogManager.getLogger(DBManager.class); // 로거
    public static Connection conn; // SQL 연결 객체

    public static String docCreateSql2 = "INSERT INTO (title, content) VALUES (?, ?)"; // 문서 생성 sql문
    public static String docReadSql = "SELECT * FROM documents WHERE id = ?"; // 문서 읽기 SQL문
    public static String docUpdateSql = "UPDATE documents SET title = ? WHERE id = ?"; // 문서 업데이트 SQL문
    public static String docDeleteSql = "DELETE FROM documents WHERE id = ?"; // 문서 삭제 SQL문

    public DBManager() throws SQLException {

        // 데이터베이스 연결 시작
        try {
            conn = DriverManager.getConnection(Constants.url, Constants.user, Constants.password);
            log.info("Connected to database.");
        } catch (SQLException e) {
            String msg = "DataBase initialization Failed.";
            log.fatal(msg, e);
            throw new SQLException(msg);
        }

    }



    // --- Document CRUD ---
//    public void createDocument(Document document) throws SQLException {
//        String sql = "INSERT INTO (title, content) VALUES (?, ?)";
//
//        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, document.getTitle());
//            pstmt.setBytes(2, document.getContent().getBytes());
//
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            String msg = "Document Create Failed.";
//            log.error(msg, e);
//            throw new SQLException(msg);
//        }
//    }



    // 문서 ID를 기반으로 문서를 찾는다.
//    public Document findDocumentById(String docId) throws SQLException {
//
//        // SQL문 -> PreparedStatement 객체 생성
//        try (PreparedStatement pstmt = conn.prepareStatement(docReadSql)) {
//
//            pstmt.setString(1, docId); // 문서 아이디 파라미터 할당
//
//            // 쿼리문 실행 -> ResultSet 객체
//            try (ResultSet rs = pstmt.executeQuery()) {
//                if (rs.next()) {
//                    String name =  rs.getString("")
//                }
//            }
//        }
//    }

    // 문서 ID 업데이트

//    public Document





    // --- User CRUD ---
    public void createUser(User user) throws SQLException {
        String createUserSql = "INSERT INTO users (id, username, password) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(createUserSql)) {
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());

            pstmt.executeUpdate(); // 출력이 없는 경우, executeUpdate().
        } catch (SQLException e) {
            String msg = "User Creation Failed.";
            log.error(msg, e);
            throw new SQLException(msg);
        }
    }

    public User findUserById(String id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);

            // ResultSet을 출력하는 경우, executeQuery()
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getString("id"), rs.getString("username"), rs.getString("password"));
                }
                return null;
            }
        } catch (SQLException e) {
            String msg = "User Read Failed.";
            log.error(msg, e);
            throw new SQLException(msg);
        }
    }

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET username = ?, password = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            String msg = "User Update Failed.";
            log.error(msg, e);
            throw new SQLException(msg);
        }
    }

    public void deleteUser(String id) throws SQLException {
        String sql = "DELETE FROM users where id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            String msg = "User Delete Failed.";
            log.error(msg, e);
            throw new SQLException(msg);
        }
    }










    // 데이터베이스 매니저 종료
    public void shutdown() throws SQLException {
        try {
            conn.close();
        } catch (SQLException e) {
            log.error("Database Closure Failed.", e);
            throw new SQLException(e);
        }
    }


}
