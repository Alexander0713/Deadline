package ru.netology.deadline;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import ru.netology.deadline.data.DataHelper;

import static com.codeborne.selenide.Selenide.open;

public class TestBase {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");

    }
    @AfterAll
    static void tearDownAll() {
        DataHelper.clearAllData();
    }
}