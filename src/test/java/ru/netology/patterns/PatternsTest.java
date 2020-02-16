package ru.netology.patterns;

import com.codeborne.selenide.Condition;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.Keys;
import ru.netology.models.UserDataModel;

import static com.codeborne.selenide.Condition.*;
import static ru.netology.patterns.UserDataGenerator.Registration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.patterns.PageElements.*;

public class PatternsTest {
    private LocalDate currentDate;
    private FakeValuesService fakeValuesService = new FakeValuesService(new Locale("ru"), new RandomService());
    private UserDataModel userDataModel;
    private int plusDays;


    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        currentDate = LocalDate.now();
        plusDays = 3 + (int) (Math.random() * 30);
        userDataModel = Registration.generateValidData("ru");
    }

    @Test
    @DisplayName("Корректный ввод после ошибочного ввода")
    void correctInputAfterIncorrect() {
        buttonNext.click();
        cityFieldHint.shouldHave(text("Поле обязательно для заполнения"));
        cityField.setValue(userDataModel.getCityPreInput());
        cities.get((int) (Math.random() * cities.size())).click();
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        buttonNext.click();
        dateFieldHint.shouldHave(text("Неверно введена дата"));
        dateField.setValue(currentDate.plusDays(plusDays).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        buttonNext.click();
        nameFieldHint.shouldHave(text("Поле обязательно для заполнения"));
        nameField.setValue(userDataModel.getUserFullName());
        buttonNext.click();
        phoneFieldHint.shouldHave(text("Поле обязательно для заполнения"));
        phoneField.setValue(userDataModel.getUserPhone());
        buttonNext.click();
        String color = checkboxAlert.getCssValue("color");
        assertEquals("rgba(255, 92, 92, 1)", color);
        checkbox.click();
        buttonNext.click();
        confirmNotification.waitUntil(visible, 15000);
        confirmNotification.shouldHave(text("Встреча успешно запланирована"));
    }

    @Test
    @DisplayName("101 буква в поле ФИО")
    void input101CharsInNameField() {
        String userFullName = fakeValuesService.regexify("[а-я]{101}");
        cityField.setValue(userDataModel.getCityPreInput());
        cities.get((int) (Math.random() * cities.size())).click();
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        dateField.setValue(currentDate.plusDays(plusDays).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        nameField.setValue(userFullName);
        phoneField.setValue(userDataModel.getUserPhone());
        checkbox.click();
        buttonNext.click();
        nameFieldHint.shouldHave(text("Имя и Фамилия указаные неверно"));
    }

    @Test
    @DisplayName("Раньше минимальной даты")
    void checkMinimalDate() {
        cityField.setValue(userDataModel.getCityPreInput());
        cities.get((int) (Math.random() * cities.size())).click();
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        dateField.setValue(currentDate.minusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        nameField.setValue(userDataModel.getUserFullName());
        phoneField.setValue(userDataModel.getUserPhone());
        checkbox.click();
        buttonNext.click();
        dateFieldHint.shouldHave(text("на выбранную дату невозможен"));
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        dateField.setValue(currentDate.plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        buttonNext.click();
        dateFieldHint.shouldHave(text("на выбранную дату невозможен"));
    }

    @ParameterizedTest
    @DisplayName("Проверка позитивных сценариев для поля Город")
    @CsvFileSource(resources = "/CitiesPositiveData.csv", numLinesToSkip = 1)
    void cityFieldCorrectInput(String city, String expected) {
        cityField.setValue(city);
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        dateField.setValue(currentDate.plusDays(plusDays).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        nameField.setValue(userDataModel.getUserFullName());
        phoneField.setValue(userDataModel.getUserPhone());
        checkbox.click();
        buttonNext.click();
        confirmNotification.waitUntil(visible, 15000);
        confirmNotification.shouldHave(text(expected));
    }

    @ParameterizedTest
    @DisplayName("Проверка позитивных сценариев для поля Имя")
    @CsvFileSource(resources = "/NamesPositiveData.csv", numLinesToSkip = 1)
    void nameFieldCorrectInput(String name, String expected) {
        cityField.setValue(userDataModel.getCityPreInput());
        cities.get((int) (Math.random() * cities.size())).click();
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        dateField.setValue(currentDate.plusDays(plusDays).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        nameField.setValue(name);
        phoneField.setValue(userDataModel.getUserPhone());
        checkbox.click();
        buttonNext.click();
        confirmNotification.waitUntil(visible, 15000);
        confirmNotification.shouldHave(text(expected));
    }

    @ParameterizedTest
    @DisplayName("Ввод букв и символов в поле Дата")
    @CsvFileSource(resources = "/IncorrectDate.csv", numLinesToSkip = 1)
    void incorrectInputDate(String incorrectDate) {
        cityField.setValue(userDataModel.getCityPreInput());
        cities.get((int) (Math.random() * cities.size())).click();
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        dateField.setValue(incorrectDate);
        dateField.shouldBe(Condition.empty);
        nameField.setValue(userDataModel.getUserFullName());
        phoneField.setValue(userDataModel.getUserPhone());
        checkbox.click();
        buttonNext.click();
        dateFieldHint.shouldHave(text("Неверно введена дата"));
    }

    @ParameterizedTest
    @DisplayName("Проверка неправильного ввода города")
    @CsvFileSource(resources = "/CitySadPath.csv", numLinesToSkip = 1)
    void sadPathCityInput(String city, String expected) {
        cityField.setValue(city);
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        dateField.setValue(currentDate.plusDays(3).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        nameField.setValue(userDataModel.getUserFullName());
        phoneField.setValue(userDataModel.getUserPhone());
        checkbox.click();
        buttonNext.click();
        cityFieldHint.shouldHave(text(expected));
    }

    @ParameterizedTest
    @DisplayName("Проверка неправильного ввода имени")
    @CsvFileSource(resources = "/NameSadPath.csv", numLinesToSkip = 1)
    void sadPathNameInput(String name, String expected) {
        cityField.setValue(userDataModel.getCityPreInput());
        cities.get((int) (Math.random() * cities.size())).click();
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        dateField.setValue(currentDate.plusDays(plusDays).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        nameField.setValue(name);
        phoneField.setValue(userDataModel.getUserPhone());
        checkbox.click();
        buttonNext.click();
        nameFieldHint.shouldHave(text(expected));
    }

    @ParameterizedTest
    @DisplayName("Проверка неправильного ввода телефона")
    @CsvFileSource(resources = "/PhoneSadPath.csv", numLinesToSkip = 1)
    void sadPathPhoneInput(String phone, String expected) {
        cityField.setValue(userDataModel.getCityPreInput());
        cities.get((int) (Math.random() * cities.size())).click();
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        dateField.setValue(currentDate.plusDays(plusDays).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        nameField.setValue(userDataModel.getUserFullName());
        checkbox.click();
        buttonNext.click();
        phoneField.setValue(phone);
        phoneFieldHint.shouldHave(text(expected));
    }

    @Test
    @DisplayName("Проверка обрезки ввода в полях Дата и Телефон")
    void trimmingStringInFields() {
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        String tooLongDate = fakeValuesService.regexify("[1-9]\\d{15}");
        Pattern datePattern = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}");
        dateField.setValue(tooLongDate);
        Matcher dateMatcher = datePattern.matcher(dateField.getAttribute("value"));
        Assertions.assertTrue(dateMatcher.matches());
        nameField.setValue(userDataModel.getUserFullName());
        String tooLongPhone = fakeValuesService.regexify("\\d{20}");
        Pattern phonePattern = Pattern.compile("\\+\\d\\s\\d{3}\\s\\d{3}\\s\\d{2}\\s\\d{2}");
        phoneField.setValue(tooLongPhone);
        Matcher phoneMatcher = phonePattern.matcher(phoneField.getAttribute("value"));
        Assertions.assertTrue(phoneMatcher.matches());
    }

    @Test
    @DisplayName("Перепланировка встречи на новую дату")
    void rescheduleOnNewDate() {
        cityField.setValue(userDataModel.getCityPreInput());
        cities.get((int) (Math.random() * cities.size())).click();
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        dateField.setValue(currentDate.plusDays(plusDays).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        nameField.setValue(userDataModel.getUserFullName());
        phoneField.setValue(userDataModel.getUserPhone());
        checkbox.click();
        buttonNext.click();
        confirmNotification.waitUntil(visible, 15000);
        confirmNotification.shouldHave(text("Встреча успешно запланирована"));
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        dateField.setValue(currentDate.plusDays(plusDays + 3).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        buttonNext.click();
        rescheduleAlert.shouldHave(text("запланирована встреча на другую дату. Перепланировать?"));
        buttonConfirmReschedule.click();
        confirmNotification.waitUntil(visible, 15000);
        confirmNotification.shouldHave(text("Встреча успешно запланирована"));
    }

    @ParameterizedTest
    @DisplayName("Перепланировка встречи на новую дату")
    @CsvFileSource(resources = "/NotificationForRescheduleOnSameDate.csv", numLinesToSkip = 1)
    void rescheduleOnSameDate(String confirmMessage, String reminderMessage) {
        cityField.setValue(userDataModel.getCityPreInput());
        cities.get((int) (Math.random() * cities.size())).click();
        dateField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        dateField.setValue(currentDate.plusDays(plusDays).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        nameField.setValue(userDataModel.getUserFullName());
        phoneField.setValue(userDataModel.getUserPhone());
        checkbox.click();
        buttonNext.click();
        confirmNotification.waitUntil(visible, 15000);
        confirmNotification.shouldHave(text(confirmMessage));
        buttonNotificationCloser.click();
        buttonNext.click();
        confirmNotification.waitUntil(visible, 15000);
        confirmNotification.shouldHave(text(reminderMessage));
    }
}