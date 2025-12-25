package ru.netology.deadline.pages;

import com.codeborne.selenide.SelenideElement;
import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement loginField = $("[data-test-id=login] input");
    private final SelenideElement passwordField = $("[data-test-id=password] input");
    private final SelenideElement loginButton = $("[data-test-id=action-login]");
    private final SelenideElement errorNotification = $("[data-test-id=error-notification]");

    public void login(String login, String password) {
        loginField.clear();
        passwordField.clear();
        loginField.setValue(login);
        passwordField.setValue(password);
        loginButton.click();
    }

    public void verifyErrorNotificationVisible(Duration timeout) {
        errorNotification.shouldBe(visible, timeout);
    }
}