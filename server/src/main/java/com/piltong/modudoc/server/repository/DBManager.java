package com.piltong.modudoc.server.repository;

import java.sql.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.piltong.modudoc.server.util.Constants;
import com.piltong.modudoc.server.model.Document;


import org.fxmisc.richtext.model.StyledDocument;


// 데이터베이스를 관리하는 클래스. MySQL Database <-> Server의 관리를 중개한다.
// 서비스 로직에서 사용하는 클래스를 정의한다. Domain 객체에 대한 CRUD를 지원한다.
public class DBManager {
    public static Logger log = LogManager.getLogger(DBManager.class); // 로거
    public static Connection conn; // SQL 연결 객체

    public static String docCreateSql = "INSERT INTO (title) VALUES (?)"; // 문서 생성 sql문
    public static String docReadSql = "SELECT * FROM documents WHERE id = ?"; // 문서 읽기 SQL문
    public static String docUpdateSql = "UPDATE documents SET title = ? WHERE id = ?"; // 문서 업데이트 SQL문

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




    // 문서 ID를 기반으로 문서를 찾는다.
//    public Document getDocument(String docId) throws SQLException {
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
