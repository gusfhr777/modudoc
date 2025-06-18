package com.piltong.modudoc.server.repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.piltong.modudoc.server.model.Document;

import java.util.List;
import java.util.Optional;

// Document 객체에 대한 CRUD 기능을 제공하는 클래스.
// DB를 통한 구현이다.
public class JDBCDocumentRepository implements DocumentRepository {

    // 필드
    private final Connection conn;
    private static final Logger log = LogManager.getLogger(JDBCDocumentRepository.class);
    private static final String INSERT_SQL = "INSERT INTO (title, content, CREATED_DATE, MODIFIED_DATE) VALUES (?, ?, ?, ?)";
    private static final String SELECT_SQL = "SELECT * FROM documents WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM documents WHERE id = ?";
    private static final String UPDATE_SQL = "UPDATE documents SET title = ?, content = ? WHERE id = ?";

    // 생성자
    public JDBCDocumentRepository() throws SQLException {
        try {
            this.conn = DBManager.getConnection();
        } catch (SQLException e) {
            String msg = "DocumentRepostiroy 초기화 실패: DBManager.GetConnection() 실패";
            log.fatal(msg, e);
            throw new SQLException(msg, e);
        }
    }

    @Override
    public Document save(Document doc) throws SQLException {
        if (doc)

        try (PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {
            pstmt.setString(1, doc.getTitle());
            pstmt.setString(2, doc.getContent());
            pstmt.setDate(3, doc.getCreatedDate());
            pstmt.setDate(4, doc.getModifiedDate());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            String msg = "Document Create Failed.";
            log.error(msg, e);
            throw new SQLException(msg);
        }
    }

    @Override
    public Optional<Document> findById(int id) {

    }

    @Override
    public List<Document> findAll() {

    }

    @Override
    public boolean delete(int id) {

    }


}
