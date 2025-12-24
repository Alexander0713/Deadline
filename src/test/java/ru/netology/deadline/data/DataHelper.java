package ru.netology.deadline.data;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/appdb";
    private static final String USER = "appuser";
    private static final String PASSWORD = "password123";

    private DataHelper() {
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL + "?useSSL=false&serverTimezone=UTC", USER, PASSWORD);
    }

    public static String getVerificationCodeForUser(String login) {
        String sql = "SELECT code FROM auth_codes ac " +
                "JOIN users u ON ac.user_id = u.id " +
                "WHERE u.login = ? " +
                "ORDER BY ac.created DESC LIMIT 1";

        QueryRunner runner = new QueryRunner();
        try (Connection conn = getConnection()) {
            return runner.query(conn, sql, new ScalarHandler<>(), login);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить код верификации для пользователя: " + login, e);
        }
    }

    public static int getAuthCodesCount() {
        QueryRunner runner = new QueryRunner();
        String sql = "SELECT COUNT(*) FROM auth_codes";

        try (Connection conn = getConnection()) {
            Long count = runner.query(conn, sql, new ScalarHandler<>());
            return count != null ? count.intValue() : 0;
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить количество записей в auth_codes", e);
        }
    }
    public static void clearAllData() {
        QueryRunner runner = new QueryRunner();
        // ВАЖНО: порядок удаления из-за foreign keys
        String[] sqlStatements = {
                "DELETE FROM card_transactions",
                "DELETE FROM auth_codes",
                "DELETE FROM cards",
                "DELETE FROM users"
        };

        try (Connection conn = getConnection()) {
            for (String sql : sqlStatements) {
                runner.update(conn, sql);
            }
            System.out.println("Все тестовые данные очищены");
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось очистить тестовые данные", e);
        }
    }
}