package ru.netology.patterns;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import ru.netology.models.UserDataModel;

import java.util.Locale;

import static java.lang.String.join;

public class UserDataGenerator {

    public static class Registration {
        private Registration() {
        }

        public static UserDataModel generateValidData(String locale) {
            Faker faker = new Faker(new Locale(locale));
            FakeValuesService fakeValuesService = new FakeValuesService(new Locale(locale), new RandomService());
            String userFullName = join(" ", faker.name().firstName(), faker.name().lastName());
            String userPhone = fakeValuesService.regexify("(\\+?)[1-9]\\d{10}");
            String cityPreInput = fakeValuesService.regexify("[оамсквт][оаеи]");
            return new UserDataModel(userFullName, userPhone, cityPreInput);
        }
    }
}


