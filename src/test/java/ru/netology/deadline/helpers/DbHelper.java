package ru.netology.deadline.helpers;

import java.sql.*;

public class DbHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/appdb";
    private static final String USER = "appuser";
    private static final String PASSWORD = "password123";

    public static String getLastAuthCodeForUser(String login) {
        String sql = "SELECT code FROM auth_codes ac " +
                "JOIN users u ON ac.user_id = u.id " +
                "WHERE u.login = ? " +
                "ORDER BY ac.created DESC LIMIT 1";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("code");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении кода из БД: " + e.getMessage());
        }
        return null;
    }

    public static void clearAuthCodes() {
        String sql = "DELETE FROM auth_codes";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Ошибка при очистке кодов: " + e.getMessage());
        }
    }

    public static int getAuthCodesCount() {
        String sql = "SELECT COUNT(*) as count FROM auth_codes";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении количества кодов: " + e.getMessage());
        }
        return 0;
    }
}