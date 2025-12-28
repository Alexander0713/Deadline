
package ru.netology.deadline.pages;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {

    private final SelenideElement codeField = $("[data-test-id=code] .input__control");
    private final SelenideElement verifyButton = $("[data-test-id=action-verify]");
    private final SelenideElement errorNotification = $("[data-test-id=error-notification]");
    private final SelenideElement errorNotificationText = $("[data-test-id=error-notification] .notification__content");

    public void verifyPageVisible(Duration timeout) {
        codeField.shouldBe(visible, timeout);
        verifyButton.shouldBe(visible, timeout);
    }

    public void enterCode(String code) {
        codeField.setValue(code);
        verifyButton.click();
    }

    public void verifyErrorNotificationNotVisible(Duration timeout) {
        errorNotification.shouldNotBe(visible, timeout);
    }

    public void verifyErrorNotificationVisible(Duration timeout) {
        errorNotificationText.shouldHave(exactText("Ошибка! Неверно указан логин или пароль"), timeout);
    }
}