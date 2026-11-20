package ru.netology.diploma.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.diploma.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentPage {
    private final SelenideElement heading = $(byText("Оплата по карте"));
    private final ElementsCollection inputs = $$(".input__control");
    private final SelenideElement cardNumberInput = inputs.get(0);
    private final SelenideElement monthInput = inputs.get(1);
    private final SelenideElement yearInput = inputs.get(2);
    private final SelenideElement holderInput = inputs.get(3);
    private final SelenideElement cvcInput = inputs.get(4);
    private final SelenideElement continueButton = $(byText("Продолжить"));

    private final SelenideElement successNotification = $(".notification_status_ok");
    private final SelenideElement errorNotification = $(".notification_status_error");

    public PaymentPage() {
        heading.shouldBe(visible);
    }

    public void fill(DataHelper.CardInfo info) {
        cardNumberInput.setValue(info.getNumber());
        monthInput.setValue(info.getMonth());
        yearInput.setValue(info.getYear());
        holderInput.setValue(info.getHolder());
        cvcInput.setValue(info.getCvc());
        continueButton.click();
    }

    public void shouldShowSuccess(String expectedText) {
        successNotification.shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text(expectedText));
    }

    public void shouldShowBankRefusal(String expectedText) {
        errorNotification.shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text(expectedText));
    }

    public void shouldShowFieldError(String fieldName, String expectedErrorText) {
        $$(".input__inner")
                .findBy(text(fieldName))
                .$(".input__sub")
                .shouldBe(visible)
                .shouldHave(text(expectedErrorText));
    }
}