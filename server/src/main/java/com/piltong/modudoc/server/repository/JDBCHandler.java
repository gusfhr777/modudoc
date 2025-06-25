package com.piltong.modudoc.server.repository;

import java.sql.*;

//import com.piltong.modudoc.common.RichTextSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


// 데이터베이스를 관리하는 클래스. Database <-> Server의 관리를 중개한다.
// 서비스 로직에서 사용하는 클래스를 정의한다. Domain 객체에 대한 CRUD를 지원한다.
// JDBC 실행 책임만을 가진다. 현재는 mySQL을 사용한다.
public class JDBCHandler {
    private static final Logger log = LogManager.getLogger(JDBCHandler.class);

    // DB 정보
    public static final String url = "jdbc:mysql://modudoc_mysql:3306/modudoc_db";
    public static final String user = "userTest";
    public static final String password = "tp81w23";


    public static Connection getConnection() throws SQLException {
        log.info("getConnection()");
        return DriverManager.getConnection(url, user, password);
    }
}

