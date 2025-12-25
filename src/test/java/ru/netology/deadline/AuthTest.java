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
        DataHelper.TestUser user = DataHelper.getTestUser();
        loginPage.login(user.getLogin(), user.getPassword());

        VerificationPage verificationPage = new VerificationPage();
        verificationPage.verifyPageVisible(Duration.ofSeconds(10));

        String code = DataHelper.getVerificationCodeForUser(user.getLogin());
        assertNotNull(code, "Код верификации не должен быть null");
        assertFalse(code.isEmpty(), "Код верификации не должен быть пустым");

        verificationPage.enterCode(code);
        verificationPage.verifyErrorNotificationNotVisible(Duration.ofSeconds(10));

        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage.verifyPageVisible(Duration.ofSeconds(10));
    }

    @Test
    void shouldShowErrorWithInvalidCredentials() {
        DataHelper.TestUser invalidUser = DataHelper.getInvalidUser();
        loginPage.login(invalidUser.getLogin(), invalidUser.getPassword());
        loginPage.verifyErrorNotificationVisible(Duration.ofSeconds(10));
    }

    @Test
    void shouldBlockUserAfterThreeInvalidAttempts() {
        DataHelper.TestUser randomUser = DataHelper.generateRandomUser();

        loginPage.login(randomUser.getLogin(), "wrongpass1");
        loginPage.verifyErrorNotificationVisible(Duration.ofSeconds(5));

        loginPage.login(randomUser.getLogin(), "wrongpass2");
        loginPage.verifyErrorNotificationVisible(Duration.ofSeconds(5));

        loginPage.login(randomUser.getLogin(), "wrongpass3");
        loginPage.verifyErrorNotificationVisible(Duration.ofSeconds(5));

        loginPage.login(randomUser.getLogin(), "anypassword");
        loginPage.verifyErrorNotificationVisible(Duration.ofSeconds(5));
    }

}