package com.piltong.modudoc.server.repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

import com.piltong.modudoc.server.model.Document;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// Document 객체에 대한 CRUD 기능을 제공하는 클래스.
// DB를 통한 구현이다.
public class JDBCDocumentRepository implements DocumentRepository {

    // 필드
    private final Connection conn;
    private static final Logger log = LogManager.getLogger(JDBCDocumentRepository.class);
    private static final String INSERT_SQL = "INSERT INTO (title, content, CREATED_DATE, MODIFIED_DATE) VALUES (?, ?, NOW(), NOW())";
    private static final String SELECT_SQL = "SELECT * FROM documents WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM documents WHERE id = ?";
    private static final String UPDATE_SQL = "UPDATE documents SET title = ?, content = ? WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM documents";

    // 생성자
    public JDBCDocumentRepository() {
        try {
            this.conn = DBManager.getConnection();
        } catch (SQLException e) {
            String msg = "DocumentRepostiroy 초기화 실패: DBManager.GetConnection() 실패";
            log.fatal(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public Document save(Document doc) {

        if (Objects.isNull(doc.getId())) { // ID가 없는 경우 -> 생성
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, doc.getTitle());
                pstmt.setString(2, doc.getContent());

                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) doc.setId(rs.getInt(1));
                }
                // DB가 갱신한 timestamp 조회
                try (PreparedStatement ts = conn.prepareStatement(
                        "SELECT created_date, modified_date FROM document WHERE id=?")) {
                    ts.setInt(1, doc.getId());
                    try (ResultSet rs = ts.executeQuery()) {
                        if (rs.next()) {
                            doc.setCreatedDate(rs.getTimestamp(1).toLocalDateTime());
                            doc.setModifiedDate(rs.getTimestamp(2).toLocalDateTime());
                        }
                    }
                }
            } catch (SQLException e) {
                String msg = "Document Create Failed.";
                log.error(msg, e);
                throw new RuntimeException(msg, e);
            }


        } else { // ID가 있는 경우 -> 업데이트
            try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)){
                pstmt.setString(1, doc.getTitle());
                pstmt.setString(2, doc.getContent());
                pstmt.setInt(3, doc.getId());

                pstmt.executeUpdate();

            } catch (SQLException e) {
                String msg = "Document Update Failed.";
                log.error(msg, e);
                throw new RuntimeException(msg, e);
            }
        }

        return doc;

    }

    @Override
    public Optional<Document> findById(int id) {
        try (PreparedStatement pstmt = conn.prepareStatement(SELECT_SQL)){
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Document(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getTimestamp(3).toLocalDateTime(), rs.getTimestamp(4).toLocalDateTime()));

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
            String msg = "Document Select Failed.";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public boolean delete(int id) {
        try (PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)){
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            String msg = "Document Select Failed.";
            log.error(msg, e);
            return false;
        }
    }
}
