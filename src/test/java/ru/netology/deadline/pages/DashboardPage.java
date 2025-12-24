package ru.netology.deadline.pages;

import com.codeborne.selenide.SelenideElement;
import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    private final SelenideElement heading = $("h2");

    public void verifyPageVisible(Duration timeout) {
        heading.shouldBe(visible, timeout);
    }
}