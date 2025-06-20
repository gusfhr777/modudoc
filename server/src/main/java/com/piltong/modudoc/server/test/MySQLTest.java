package com.piltong.modudoc.server.test;

import java.sql.*;

public class MySQLTest {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://192.168.219.106:3306/modudoc_db";
        String username = "userTest";
        String password = "tp81w23";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
            System.out.println("✅ DB 연결 성공!");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT NOW()");

            if (rs.next()) {
                System.out.println("현재 시간: " + rs.getString(1));
            }

        } catch (SQLException e) {
            System.out.println("❌ 연결 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
