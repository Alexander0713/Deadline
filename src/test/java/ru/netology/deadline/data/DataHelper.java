package ru.netology.deadline.data;

import com.github.javafaker.Faker;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;

public class DataHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/appdb";
    private static final String USER = "appuser";
    private static final String PASSWORD = "password123";
    private static final Faker faker = new Faker(new Locale("ru"));

    private DataHelper() {
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL + "?useSSL=false&serverTimezone=UTC", USER, PASSWORD);
    }

    public static TestUser getTestUser() {
        return new TestUser("vasya", "qwerty123");
    }

    public static TestUser generateRandomUser() {
        String login = faker.name().username().toLowerCase().replaceAll("[^a-z0-9]", "");
        String password = faker.internet().password(8, 12, true, true, true);
        return new TestUser(login, password);
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
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось очистить тестовые данные", e);
        }
    }

    @Data
    @RequiredArgsConstructor
    public static class TestUser {
        private final String login;
        private final String password;
    }
}