package ru.netology.patterns;

import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Locale;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class PatternsTest {
    private SelenideElement form;
    private SelenideElement root;
    private SelenideElement cityField;
    private SelenideElement dateField;
    private SelenideElement nameField;
    private SelenideElement phoneField;
    private SelenideElement checkbox;
    private SelenideElement buttonNext;
    private LocalDate currentDate;
    private SelenideElement body;
    private Faker faker;
    private String userName;
    private String userPhone;
    private FakeValuesService fakeValuesService;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        root = $("#root");
        form = $("form");
        cityField = form.$("input[placeholder=Город]");
        dateField = form.$("input[placeholder='Дата встречи']");
        nameField = form.$("input[name=name]");
        phoneField = form.$("input[name=phone]");
        checkbox = form.$(".checkbox[data-test-id=agreement]");
        buttonNext = form.$(byText("Забронировать"));
        currentDate = LocalDate.now();
        body = $("body");
        faker = new Faker(new Locale("ru"));
        userName = faker.name().fullName();
        fakeValuesService = new FakeValuesService(new Locale("ru"), new RandomService());
        userPhone = fakeValuesService.regexify("(\\+)\\d{11}");
    }

    @Test
    void print() {
        System.out.println(userName);
        System.out.println(userPhone);
    }



}
