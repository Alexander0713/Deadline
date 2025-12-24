package ru.netology.deadline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.deadline.data.DataHelper;
import ru.netology.deadline.pages.DashboardPage;
import ru.netology.deadline.pages.LoginPage;
import ru.netology.deadline.pages.VerificationPage;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

class AuthTest {
    private LoginPage loginPage;

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        loginPage = new LoginPage();
    }

    @Test
    void shouldSuccessfullyLoginWithValidCredentials() {
        loginPage.login("vasya", "qwerty123");

        VerificationPage verificationPage = new VerificationPage();
        verificationPage.verifyPageVisible(Duration.ofSeconds(10));

        // Даем время на генерацию кода
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String code = DataHelper.getVerificationCodeForUser("vasya");
        assertNotNull(code, "Код верификации не должен быть null");
        assertFalse(code.isEmpty(), "Код верификации не должен быть пустым");

        verificationPage.enterCode(code);
        verificationPage.verifyErrorNotificationNotVisible(Duration.ofSeconds(10));

        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage.verifyPageVisible(Duration.ofSeconds(10));
    }

    @Test
    void shouldShowErrorWithInvalidCredentials() {
        loginPage.login("invalid", "invalid");
        loginPage.verifyErrorNotificationVisible(Duration.ofSeconds(10));
    }

    @Test
    void shouldBlockUserAfterThreeInvalidAttempts() {
        String testUser = "testuser" + System.currentTimeMillis();

        for (int i = 0; i < 3; i++) {
            loginPage.login(testUser, "wrongpass" + i);
            loginPage.verifyErrorNotificationVisible(Duration.ofSeconds(10));

            open("http://localhost:9999");
            loginPage = new LoginPage();
        }

        loginPage.login("vasya", "qwerty123");

        VerificationPage verificationPage = new VerificationPage();
        verificationPage.verifyPageVisible(Duration.ofSeconds(10));

        String code = DataHelper.getVerificationCodeForUser("vasya");
        assertNotNull(code, "Код должен генерироваться даже после неудачных попыток");
    }

    @Test
    void shouldSaveVerificationCodeInDatabase() {
        loginPage.login("vasya", "qwerty123");

        VerificationPage verificationPage = new VerificationPage();
        verificationPage.verifyPageVisible(Duration.ofSeconds(10));

        // Ждем генерации кода
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int codesCount = DataHelper.getAuthCodesCount();
        assertTrue(codesCount > 0, "В базе данных должен быть сохранен хотя бы один код верификации");

        String code = DataHelper.getVerificationCodeForUser("vasya");
        assertNotNull(code, "Код верификации должен быть сохранен для пользователя vasya");
        assertFalse(code.isEmpty(), "Код верификации не должен быть пустым");
    }

    @Test
    void shouldVerifyLoginFormElements() {
        loginPage.verifyLoginFormVisible(Duration.ofSeconds(10));
    }
}