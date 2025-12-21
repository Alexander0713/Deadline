package ru.netology.deadline;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import ru.netology.deadline.helpers.DbHelper;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;

public class AuthTest extends TestBase {

    @Test
    void shouldSuccessfullyLoginWithDemoUserAndRealCodeFromDb() {

        $("[data-test-id=login] input").clear();
        $("[data-test-id=password] input").clear();
        $("[data-test-id=login] input").setValue("vasya");
        $("[data-test-id=password] input").setValue("qwerty123");
        $("[data-test-id=action-login]").click();
        $("[data-test-id=code] input").shouldBe(Condition.visible);

        String codeFromDb = DbHelper.getLastAuthCodeForUser("vasya");

        assert codeFromDb != null && !codeFromDb.isEmpty() : "Код из БД не должен быть пустым";

        $("[data-test-id=code] input").setValue(codeFromDb);
        $("[data-test-id=action-verify]").click();
        $("[data-test-id=error-notification]").shouldNotBe(Condition.visible);
    }

    @Test
    void shouldTestInvalidCredentials() {
        $("[data-test-id=login] input").clear();
        $("[data-test-id=password] input").clear();
        $("[data-test-id=login] input").setValue("wronguser");
        $("[data-test-id=password] input").setValue("wrongpass");
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldBe(Condition.visible);
    }

    @Test
    void shouldTestThreeFailedAttemptsBlocking() {
        String testLogin = "testuser" + System.currentTimeMillis();

        for (int i = 0; i < 3; i++) {
            $("[data-test-id=login] input").clear();
            $("[data-test-id=password] input").clear();
            $("[data-test-id=login] input").setValue(testLogin);
            $("[data-test-id=password] input").setValue("wrongpass" + i);
            $("[data-test-id=action-login]").click();
            $("[data-test-id=error-notification]").shouldBe(Condition.visible);
        }

        $("[data-test-id=login] input").clear();
        $("[data-test-id=password] input").clear();
        $("[data-test-id=login] input").setValue(testLogin);
        $("[data-test-id=password] input").setValue("anypassword");
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldBe(Condition.visible);
    }

    @Test
    void shouldVerifyAuthCodeIsSavedInDatabase() {
        $("[data-test-id=login] input").clear();
        $("[data-test-id=password] input").clear();
        $("[data-test-id=login] input").setValue("vasya");
        $("[data-test-id=password] input").setValue("qwerty123");
        $("[data-test-id=action-login]").click();
        $("[data-test-id=code] input").shouldBe(Condition.visible);

        int codesCount = DbHelper.getAuthCodesCount();
        assert codesCount > 0 : "После попытки входа должен быть хотя бы один код в БД";

        String code = DbHelper.getLastAuthCodeForUser("vasya");
        assert code != null && !code.isEmpty() : "Код для пользователя vasya не должен быть пустым";

        $("[data-test-id=code] input").setValue(code);
        $("[data-test-id=action-verify]").click();
        $("[data-test-id=error-notification]").shouldNotBe(Condition.visible);
    }

    @Test
    void shouldTestLoginFormElements() {
        // Проверяем наличие всех необходимых элементов
        $("[data-test-id=login] input").shouldBe(Condition.visible);
        $("[data-test-id=password] input").shouldBe(Condition.visible);
        $("[data-test-id=action-login]").shouldBe(Condition.visible);
        $("[data-test-id=login] input").shouldHave(Condition.attribute("type", "text"));
        $("[data-test-id=password] input").shouldHave(Condition.attribute("type", "password"));
        $("h2").shouldBe(Condition.visible);
    }
}