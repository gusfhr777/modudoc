package com.piltong.modudoc.server.repository;


import com.piltong.modudoc.server.repository.exception.document.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;

import com.piltong.modudoc.server.model.Document;

    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;

// Document 객체에 대한 CRUD 기능을 제공하는 클래스.
// DB를 통한 구현이다.
public class JDBCDocumentRepository implements DocumentRepository {

    // 필드
    private final Connection conn;
    private static final Logger log = LogManager.getLogger(JDBCDocumentRepository.class);
    private static final String INSERT_SQL = "INSERT INTO documents (title, content, CREATED_DATE, MODIFIED_DATE) VALUES (?, ?, NOW(), NOW())";
    private static final String SELECT_SQL = "SELECT * FROM documents WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM documents WHERE id = ?";
    private static final String UPDATE_SQL = "UPDATE documents SET title = ?, content = ? WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM documents";

    // 생성자
    public JDBCDocumentRepository() {
        try {
            this.conn = DBManager.getConnection();
        } catch (SQLException e) {
            String msg = "DocumentRepository 초기화 실패: DBManager.GetConnection() 실패";
            log.fatal(msg, e);
            throw new RuntimeException(msg, e);
        }
    }



    // 문서 생성 메서드
    @Override
    public Document save(Document document) {
        // 파라미터 검사
        if (document == null) { // 파라미터 검사
            String errMsg = "document is null.";
            throw new IllegalArgumentException(errMsg);
        }

        // 값 업데이트
        document.setModifiedDate(LocalDateTime.now()); // 수정일 업데이트
        if (document.getTitle() == null) {
            document.setTitle("");
        }
        if (document.getContent() == null) {
            document.setContent("");
        }
        if (document.getCreatedDate() == null) {
            document.setCreatedDate(LocalDateTime.now());
        }

        if (document.getId() == null) { // ID가 없는 경우 -> 생성 후, 문서 필드 대입.
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, document.getTitle());
                pstmt.setString(2, document.getContent());

                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) document.setId(rs.getInt(1));
                }

                // DB가 갱신한 timestamp 조회
                try (PreparedStatement ts = conn.prepareStatement(
                        "SELECT created_date, modified_date FROM document WHERE id=?")) {
                    ts.setInt(1, document.getId());
                    try (ResultSet rs = ts.executeQuery()) {
                        if (rs.next()) {
                            document.setCreatedDate(rs.getTimestamp(1).toLocalDateTime());
                            document.setModifiedDate(rs.getTimestamp(2).toLocalDateTime());
                        }
                    }
                }
            } catch (SQLException e) {
                String msg = "JDBC insert or select fail.";
                throw new DocumentSaveException(msg, e);
            }

        } else { // ID가 있는 경우 -> 업데이트
            try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)){
                pstmt.setString(1, document.getTitle());
                pstmt.setString(2, document.getContent());
                pstmt.setInt(3, document.getId());

                pstmt.executeUpdate();

            } catch (SQLException e) {
                String msg = "JDBC update fail.";
                throw new DocumentSaveException(msg, e);
            }
        }

        return document;

    }

    @Override
    public Optional<Document> findById(Integer id) {

        if (id == null) {
            String msg = "document id is null.";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        try (PreparedStatement pstmt = conn.prepareStatement(SELECT_SQL)){
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Document doc = new Document(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("content"),
                            rs.getTimestamp("CREATED_DATE").toLocalDateTime(),
                            rs.getTimestamp("MODIFIED_DATE").toLocalDateTime()
                    );
                    return Optional.of(doc);
                }
            }

            return Optional.empty(); // 값이 없는 경우
        } catch (SQLException e) {
            String msg = "JDBC select failed.";
            throw new DocumentReadException(msg, e);
        }
    }

    @Override
    public List<Document> findAll() {
        List<Document> list = new ArrayList<Document>();

        try (PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_SQL)){
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Document(
                                    rs.getInt(1),
                                    rs.getString(2),
                                    rs.getString(3),
                                    rs.getTimestamp(3).toLocalDateTime(),
                                    rs.getTimestamp(4).toLocalDateTime()));
                }
            }
            return list;
        } catch (SQLException e) {
            String msg = "JDBC select all failed.";
            throw new DocumentReadException(msg, e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)){
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) { // 행이 0개인 경우
                return false;
            } else if (affectedRows == 1) {
                return true;
            } else {
                String msg = "JDBC deleted rows are above 1. DB Integrity error.";
                throw new IllegalStateException(msg);
            }
        } catch (SQLException e) {
            String msg = "JDBC delete failed.";
            throw new DocumentDeleteException(msg, e);
        }
    }
}
