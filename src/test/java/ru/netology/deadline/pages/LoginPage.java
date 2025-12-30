package ru.netology.deadline.pages;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement loginField = $("[data-test-id=login] input");
    private final SelenideElement passwordField = $("[data-test-id=password] input");
    private final SelenideElement loginButton = $("[data-test-id=action-login]");
    private final SelenideElement errorNotification = $("[data-test-id=error-notification]");
    private final SelenideElement errorNotificationText = $("[data-test-id=error-notification] .notification__content");
    private final SelenideElement blockedNotification = $("[data-test-id=blocked-notification]");
    private final SelenideElement blockedNotificationText = $("[data-test-id=blocked-notification] .notification__content");

    public void login(String login, String password) {
        loginField.setValue(login);
        passwordField.setValue(password);
        loginButton.click();
    }


    public void verifyBlockedUserMessageVisible(Duration timeout) {
        blockedNotification.shouldBe(visible, timeout);
        blockedNotificationText.shouldHave(
                text("Пользователь заблокирован"),timeout);
    }

    public void verifyErrorNotificationVisible(Duration timeout) {
        errorNotificationText.shouldHave(text("Ошибка"), timeout);
        errorNotificationText.shouldHave(text("Неверно указан логин или пароль"), timeout);
    }

    public void verifyLoginFormVisible(Duration timeout) {
        loginField.shouldBe(visible, timeout);
        passwordField.shouldBe(visible, timeout);
        loginButton.shouldBe(visible, timeout);

    }
}