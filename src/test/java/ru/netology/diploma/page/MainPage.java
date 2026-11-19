package ru.netology.diploma.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final SelenideElement buyButton = $(byText("Купить"));

    public PaymentPage openPaymentForm() {
        buyButton.click();
        return new PaymentPage();
    }
}