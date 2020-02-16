package ru.netology.patterns;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class PageElements {
    static final SelenideElement root = $("#root");
    static final SelenideElement body = $("body");
    static final SelenideElement form = $("form");
    static final SelenideElement cityField = form.$("input[placeholder=Город]");
    static final SelenideElement dateField = form.$("input[placeholder='Дата встречи']");
    static final SelenideElement nameField = form.$("input[name=name]");
    static final SelenideElement phoneField = form.$("input[name=phone]");
    static final SelenideElement checkbox = form.$(".checkbox[data-test-id=agreement]");
    static final SelenideElement buttonNext = form.$(".button_size_m").$(byText("Запланировать"));
    static final SelenideElement cityFieldHint = form.$("[data-test-id=city] .input__sub");
    static final SelenideElement dateFieldHint = form.$(".input[data-test-id=date], .input_invalid .input__sub");
    static final SelenideElement nameFieldHint = form.$("[data-test-id=name] .input__sub");
    static final SelenideElement phoneFieldHint = form.$("[data-test-id=phone] .input__sub");
    static final SelenideElement checkboxAlert = form.$(".input_invalid .checkbox__text");
    static final SelenideElement confirmNotification = root.$(".notification[data-test-id=success-notification]");
    static final SelenideElement rescheduleAlert = root.$(".notification[data-test-id=replan-notification]");
    static final SelenideElement buttonConfirmReschedule = root.$(".button_size_s").$(byText("Перепланировать"));
    static final SelenideElement buttonNotificationCloser = root.$(".notification__closer");
    static final ElementsCollection cities = body.$$(".popup_height_adaptive .menu .menu-item__control");
}
